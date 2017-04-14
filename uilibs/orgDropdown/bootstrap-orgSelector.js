/**
 * 部门下拉选择框
 * 依赖： jquery， bootstrap
 * 选择部门通过 $ele.data('selOrg')获取部门信息对象
 */
;
(function($, window) {
	var pluginName = 'gmOrgSelector';
	var defaults = {
		'chkStyle': '',
		'chkboxType': {
			"Y": "ps",
			"N": "ps"
		},
		'showPolice': false,
		'children': 'children',
		'name': 'name',
		'onSelect': null,
		'initName': null
	};

	var count = 0;

	var OrgSelector = function($ele, settings) {
		this.$ele = $ele;
		this.settings = $.extend({}, defaults, settings);
		this.index = count++;
	};

	OrgSelector.prototype = {
		init: function() {
			var selectstr = '<div class="input-group">';
			selectstr += '<input type="text" class="form-control" readonly style="background-color: #FFF;">';
			selectstr += '<div class="input-group-btn">';
			selectstr += '<button type="button" class="btn btn-default">';
			selectstr += '<span class="caret"></span>';
			selectstr += '</button>';
			selectstr += '</div>';
			selectstr += '</div>'

			selectstr += '<div class="menuContainer" style="display:none; position: absolute; top: 34px;left: 0;width: 240px;height: 350px;overflow: auto;border: 1px solid #CCCCCC;z-index:99;background-color:#FFF;">';
			selectstr += '<ul id="ztree' + this.index + '" class="ztree ztree-police" style="margin-top:0;background-color:white;"></ul>';
			selectstr += '</div>';

			this.$ele.append(selectstr);

			var setting = {
				data: {
					keep: {
						leaf: false,
						parent: false
					},
					key: {
						name: this.settings.name,
						children: this.settings.children
					}
				},
				check: {
					enable: this.settings.chkStyle != '',
					chkStyle: this.settings.chkStyle,
					chkboxType: this.settings.chkboxType
				},
				view: {
					dblClickExpand: false,
					selectedMulti: false
				},
				callback: {
					onClick: this.settings.chkStyle == '' ? this.onSelect.bind(this) : null,
					onCheck: this.settings.chkStyle != '' ? this.onCheck.bind(this) : null,
					onExpand: this.settings.showPolice ? this.onExpand.bind(this) : null
				}
			};

			var that = this;

			$.ajax({
				url: "/gmvcshomepage/system/user/orgTree.action",
				type: "get",
				contentType: "application/json",
				dataType: 'json',
				async: true,
				cache: false,
				error: function() {
					return;
				},

				success: function(ret) {
					if(ret.headers.ret != 0) {
						ret.body = [];
					}

					that.settleData(ret.body);

					$.fn.zTree.init($("#ztree" + that.index), setting, ret.body);

					that.treeObj = $.fn.zTree.getZTreeObj("ztree" + that.index);

					var $input = $("input", that.$ele);
					$input.attr('value', ret.body[0][that.settings.name]);
					that.$ele.data('selOrg', ret.body[0]);

					$('.input-group', that.$ele).on('click', that.showMenu.bind(that));

					that.settings.onSelect && that.settings.onSelect(ret.body[0]);
					if(that.settings.initName) {
						$input.attr('value', that.settings.initName); //初始化所属部门
					}
				}
			});
		},
		settleData: function(data) {

			var that = this;
			data.forEach(function(value) {
				if(value.children) {
					value.nocheck = true;
					value.isParent = true;
					value.orgId = value.id;
					value.displayName = value.name;

					if(value.children.length != 0) {
						that.settleData(value.children);
					}
				}
			});
		},
		showMenu: function() {
			$(".menuContainer", this.$ele).slideDown('fast');
			$("body").bind("mousedown", this.onBodyDown.bind(this));
		},
		hideMenu: function() {
			//$(".menuContainer", this.$ele).fadeOut("fast");
			$(".menuContainer", this.$ele).slideUp("fast");
			$("body").unbind("mousedown", this.onBodyDown.bind(this));
		},
		onBodyDown: function(event) {
			if($(event.target).closest(this.$ele).length > 0) {
				return;
			}

			this.hideMenu();
		},
		onSelect: function(event, treeId, treeNode) {

			if(typeof treeNode.enable !== undefined && treeNode.enable === false) {
				return false;
			}

			var zTree = $.fn.zTree.getZTreeObj(treeId);
			var nodes = zTree.getSelectedNodes();

			var $input = $("input", this.$ele);
			$input.attr('value', nodes[0][this.settings.name]);
			//this.$ele.data('selOrg', {'name':nodes[0][this.settings.name], 'orgId':nodes[0].orgCode});
			this.$ele.data('selOrg', nodes[0]);
			this.hideMenu();

			this.settings.onSelect && this.settings.onSelect(treeNode);
		},
		onCheck: function(event, treeId, treeNode) {
			var zTree = $.fn.zTree.getZTreeObj(treeId);
			var nodes = zTree.getCheckedNodes(true);

			var str = "";
			var selorgs = [];
			for(var i = 0; i < nodes.length; ++i) {
				str += nodes[i][this.settings.name] + ",";
				selorgs[i] = nodes[i]; //{name: nodes[i][this.settings.name], orgId: nodes[i].orgCode};
			}

			if(str.length > 0) {
				str = str.substring(0, str.length - 1);
			}

			var $input = $("input", this.$ele);
			$input.attr('value', str);
			this.$ele.data('selOrg', selorgs);

			//this.settings.onSelect && this.settings.onSelect(treeNode); 
		},
		onExpand: function(event, treeId, treeNode) {

			if(treeNode.loaded) {
				return;
			}

			var that = this;

			$.ajax({
				type: "post",
				url: '/gmvcshomepage/system/user/orgUserState.action',
				data: JSON.stringify({
					org_ids: [treeNode.orgId]
				}),
				contentType: "application/json",
				dataType: 'json',
				async: true,
				cache: false,
				error: function(ret) {
					return;
				},
				success: function(ret) {
					treeNode.loaded = true;
					if(ret.headers.ret != 0) {
						ret.body[0].userInfo = [];
					}

					that.treeObj.addNodes(treeNode, ret.body[0].userInfo || []);
				}
			});
		}
	};

	$.fn.gmOrgSelector = function(settings, data) {
		var orgSelector = new OrgSelector(this.length > 1 ? $(this[0]) : this, settings, data);

		orgSelector.init();
	};

}(jQuery, window))