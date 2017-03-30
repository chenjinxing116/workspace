package com.goldmsg.gmdoc.entity.solr;

import com.goldmsg.coolsearch.bean.XDocumentParams;
import org.apache.solr.client.solrj.beans.Field;

public class GoldmsgDocument extends XDocumentParams {

    @Field
    private String doc_code;
    @Field
    private String doc_title;
    @Field
    private String doc_type;
    @Field
    private String doc_size;
    @Field
    private String[] doc_label;
    @Field
    private int doc_cato_id;
    @Field
    private int upload_user_id;
    @Field
    private int security_level;

    public String getDoc_code() {
        return doc_code;
    }

    public void setDoc_code(String doc_code) {
        this.doc_code = doc_code;
        setId(doc_code);
    }

    public String getDoc_title() {
        return doc_title;
    }

    public void setDoc_title(String doc_title) {
        this.doc_title = doc_title;
    }

    public String getDoc_type() {
        return doc_type;
    }

    public void setDoc_type(String doc_type) {
        this.doc_type = doc_type;
    }

    public String getDoc_size() {
        return doc_size;
    }

    public void setDoc_size(String doc_size) {
        this.doc_size = doc_size;
    }

    public String[] getDoc_label() {
        return doc_label;
    }

    public void setDoc_label(String[] doc_label) {
        this.doc_label = doc_label;
    }

    public int getDoc_cato_id() {
        return doc_cato_id;
    }

    public void setDoc_cato_id(int doc_cato_id) {
        this.doc_cato_id = doc_cato_id;
    }

    public int getUpload_user_id() {
        return upload_user_id;
    }

    public void setUpload_user_id(int upload_user_id) {
        this.upload_user_id = upload_user_id;
    }

    public int getSecurity_level() {
        return security_level;
    }

    public void setSecurity_level(int security_level) {
        this.security_level = security_level;
    }
}
