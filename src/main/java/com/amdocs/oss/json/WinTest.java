package com.amdocs.oss.json;

import java.util.HashMap;
import java.util.Map;

public class WinTest {

    public static void main(String[] args)
    {


         String pp1 = "{\n" +
                 "\t\"user\":\"vz\",\n" +
                 "\t\"password\":\"biginjapan1!\",\n" +
                 "\t\"attributes\":[1,2,3,1,\"sn\",\"mail\"],\n" +
                 "\t\"query\":\"(&(objectClass=${objectClass})(sAMAccountName=${userName}))\",\n" +
                 "\t\"parameters\":{\"userName\":\"gadbi\",\"objectClass\":\"user\",\"value\":[\"k1\",\"K1\"]}\n" +
                 "}";

         String pp2 = "{\n" +
                 "\t\"user\":\"vz\",\n" +
                 "\t\"password\":\"biginjapan1!\",\n" +
                 "\t\"attributes\":[1,2,3,1,\"sn\",\"mail\"],\n" +
                 "\t\"query\":\"(&(objectClass=${objectClass})(sAMAccountName=${userName}))\",\n" +
                 "\t\"parameters\":{\n" +
                 "\t\t\n" +
                 "\t\t    \"userName\":\"gadbi\",\n" +
                 "\t\t\t\"objectClass\":\"user\",\n" +
                 "\t\t\t\"value\":[\"k\",\"K\"]\n" +
                 "\t\t\t\n" +
                 "\t\t}\n" +
                 "\n" +
                 "}";
          //  readValue(pp1);

        HashMap<?,?> parameters  = null;
        HashMap<Object,Object> nn = new HashMap<>();
        nn.put("Gad","Biran");
        parameters = nn;


    }

    public static Map<String,?> readValue(String json)
    {
        Map<String,Object> jsonMap = new HashMap<>();
        json = json.replaceAll("([\b \t \n \f \r ])","");
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

        if(json.length() > 3)
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
