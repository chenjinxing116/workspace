package com.goldmsg.core.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;

/**
 * User: XuJiao
 * Date: 14-7-2
 * Time: 下午3:51
 */
public class INIReader
{

        protected HashMap sections = new HashMap();
        private transient String currentSecion;
        private transient Properties current;

        public INIReader(String filename)
        {
            try
            {
                BufferedReader reader = new BufferedReader(new FileReader(filename));
                read(reader);
                reader.close();
            }
            catch(IOException e)
            {
                e.printStackTrace();
            }
        }

    protected void read(BufferedReader reader) throws IOException
    {
        String line;
        while ((line = reader.readLine()) != null)
        {
            parseLine(line);
        }
        sections.put(currentSecion, current); //这里加这行，防止最后一个section没处理
    }

    protected void parseLine(String line)
    {
        line = line.trim();
        if (line.startsWith("[")&&line.endsWith("]"))
        {
            if (current != null)
            {
                sections.put(currentSecion, current);
            }
            currentSecion = line;
            current = new Properties();
        }
        else
        {
            int i = line.indexOf('=');
            if(i==-1) return;
            String name = line.substring(0, i).trim();
            String value = line.substring(i + 1).trim();
            current.setProperty(name, value);
        }
    }

    public String getValue(String section, String name)
    {
        Properties p = (Properties) sections.get("["+section+"]");//这样也可以处理replace"[","]"问题

        if (p == null)
        {
            return null;
        }

        String value = p.getProperty(name);
        return value;
    }
}
