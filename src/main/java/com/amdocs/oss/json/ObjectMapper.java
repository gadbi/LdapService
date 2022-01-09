package com.amdocs.oss.json;

import com.amdocs.oss.annotation.Setter;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.lang.reflect.Field;

public class ObjectMapper {

    /**
     * Convert Json String to POJO class
     * @param jsonInString
     * @param cls
     * @param <T>
     * @return
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws ClassNotFoundException
     */
    public <T> T readValue(String jsonInString, Class<T> cls) throws IllegalAccessException, InstantiationException, ClassNotFoundException {

        Map<String,?> user = readValue(jsonInString);
        Method [] methods = cls.getMethods();
        Object o = Class.forName(cls.getName()).newInstance();
        for(int i =0 ; i < methods.length;i++)
        {
            Setter setter = methods[i].getAnnotation(Setter.class);
            if(setter != null) {
                Method method = methods[i];
                String path = setter.path();
                user.forEach((k, v) -> {
                    String key = k.replaceAll("([\b \t \n \f \r \\\" \\' \\, \\\\])","");
                    Object value = v;//.toString().replaceAll("([\b \t \n \f \r \\\" \\' \\\\])","");
                    if (setter.path().equalsIgnoreCase(key)) {

                            try {
                                method.invoke(o, value);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                    }
                });
            }
            //if(setter.method().equalsIgnoreCase(user.))
        }


        return cls.cast(o);
    }

    /**
     * Convert class to Json
     * @param cls<Class>
     * @return
     */
    public String readValue(Class cls)
    {
        StringBuilder json = new StringBuilder();
        json.append("{");
        Field [] fields = cls.getClass().getFields();
        Object sourceObject;
        boolean flag = false;
        for(Field field : fields){
            try {
                if(flag){
                    json.append(",");
                }
                flag = true;
                sourceObject = field.get(cls);
                if(sourceObject == null || field.getType().toString().equals("long"))
                {
                    json.append( '"' + field.getName() + '"' + ":" + sourceObject);
                }
                else{
                    json.append( '"' + field.getName() + '"' + ":" + '"' + sourceObject + '"' );
                }

            }catch (IllegalArgumentException | IllegalAccessException e) {
                return null;
            }

        }

        json.append("}");
        return json.toString();
    }

/*
    public Map<String,String> readValue(String jsonInString){

        String jsonStringValue = jsonInString.substring(1, jsonInString.length()-1);
        String[] keyValuePair = jsonStringValue.split(",");
        Map<String,String> jsonMap = new HashMap<>();
        for(String pair : keyValuePair)
        {
            String[] entry = pair.split(":");
            String key  = entry[0].replaceAll("([$ { } \b \t \n \f \r \\\" \\' \\\\])","");
            String value = entry[1].replaceAll("([$ { } \b \t \n \f \r \\\" \\' \\\\])","");
            jsonMap.put(key,value);

        }
        return jsonMap;
    }*/

    public static Map<String,?> readValue(String json)
    {
        json = json.replaceAll("([\b \t \n \f \r ])","");
        Map<String,Object> jsonMap = new HashMap<>();
        jsonParser(json,jsonMap);
        return jsonMap;
    }
    public static void jsonParser(String json,Map<String,Object> jsonMap)
    {
        int isKey =    (json.indexOf("\":\"") == -1)?8000000:json.indexOf("\":\"");
        int isArray = (json.indexOf("\":[") == -1)?8000000:json.indexOf("\":[");
        int isObject = (json.indexOf("\":{") == -1)?8000000:json.indexOf("\":{");

        if(isKey < isObject && isKey < isArray){
            json = getKeyVale(json,jsonMap);
        }
        if(isArray < isKey && isArray < isObject )
        {
            json = getArrayValue(json,jsonMap);
        }
        if(isObject < isKey && isObject < isArray)
        {
            json = getObjectArray(json,jsonMap);
        }

        if(json.length() > 5)
        {
            jsonParser(json,jsonMap);
        }
    }

    public static String getKeyVale(String json,Map<String,Object> jsonMap)
    {
        int firstMarks = json.indexOf("\"");
        int lastMarksAsSingle  = json.indexOf("\",");
        int lastMarksAsObject = json.indexOf("},");
        int lastMarksAsCloseObject = json.indexOf("}");
        int lastMarks = 0;
        if(lastMarksAsSingle != -1 && lastMarksAsSingle < lastMarksAsObject) {
            lastMarks = lastMarksAsSingle;
        }else {
            if (lastMarksAsObject == -1 && lastMarksAsSingle != -1) {
                lastMarks = lastMarksAsSingle;
            }else{
                lastMarks = lastMarksAsObject;
            }
        }

        if(lastMarks == -1){
            lastMarks = lastMarksAsCloseObject;
        }





        String strToSplit = json.substring(firstMarks + 1,lastMarks);
        String delimtar = (strToSplit.contains("\":\""))?"\":\"":":";

        String [] keyVal = json.substring(firstMarks + 1,lastMarks).split(delimtar);
        jsonMap.put(keyVal[0], keyVal[1]);
        json = json.substring(lastMarks + 1);
        return json;
    }



    public static String getArrayValue(String json,Map<String,Object> jsonMap)
    {

        int firstMarks = json.indexOf("\"");
        int lastMarks  = json.indexOf("],");

        lastMarks = (lastMarks == -1)?json.indexOf("]}")+1:lastMarks;


        if(lastMarks == 0){
            json = json+"}";
            lastMarks = json.indexOf("]}")+1;
        }

        String jsonArrayString = json.substring(firstMarks + 1,lastMarks);
        if (jsonArrayString.contains("{")){

            Map<String,Object> jsonObjectMap = new HashMap<>();
            String key = jsonArrayString.substring(0,jsonArrayString.indexOf("\":"));
            jsonArrayString = jsonArrayString.substring(jsonArrayString.indexOf("\":"));
            jsonArrayString = jsonArrayString.substring(jsonArrayString.indexOf("[")+1);
            jsonParser(jsonArrayString,jsonObjectMap);
            jsonMap.put(key,jsonObjectMap);
        }else {

            String [] keyVal = jsonArrayString.split("\":\\[");
            jsonMap.put(keyVal[0], getSplitObjectAsArray(keyVal[1]));
        }
        json = json.substring(lastMarks + 1);
        return json;
    }

    public static String []  getSplitObjectAsArray(String strArray)
    {
        strArray = strArray.replaceAll("([$ { } \b \t \n \f \r \\\" \\' ])","");
        strArray = strArray.replace("]","");
        return strArray.split(",");
    }

    public static String getObjectArray(String json,Map<String,Object> jsonMap)
    {

        String key = json.substring(0,json.indexOf("\":"));
        int  firstIndex = json.indexOf("{");
        char []  jsonAsCharArray = json.toCharArray();
        int index = 0;
        int totalOpenMarked = 0;
        for(char tav : jsonAsCharArray)
        {
            index++;
            totalOpenMarked = (tav == '{')?totalOpenMarked + 1:totalOpenMarked;
            totalOpenMarked =  (tav == '}')?totalOpenMarked - 1:totalOpenMarked;

            if(totalOpenMarked == 0 || totalOpenMarked == -1)
            {
                if(index > firstIndex) {
                    break;
                }
            }

        }
        Map<String,Object> jsonObjectMap = new HashMap<>();
        String jsonNewString = json.substring(firstIndex + 1);
        jsonParser(jsonNewString,jsonObjectMap);
        jsonMap.put(key,jsonObjectMap);
        json = json.substring(index +1);
        return json;
    }

}
