package com.digdes.school;

import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        JavaSchoolStarter starter = new JavaSchoolStarter();
        try {
            // insert test
//            List<Map<String, Object>> result1 = starter.execute("INSERT VALUES 'lastName' = 'ale', 'id'=1 , 'age'= 135, 'active'= false, 'cost'=5.1");
//            List<Map<String, Object>> result2 = starter.execute("inseRT values 'cost' =   1.2  ,'lastName'='Пирожков',  'age'=  50 , 'id'=2 , 'active'=  true");
//            List<Map<String, Object>> result3 = starter.execute("  INSERT     values    'cost   ' =   1000.1  ,'   lastName   '='popoa'   ,  'age  '= 1 , '  id'= 3 , 'active  '=  true");
//            List<Map<String, Object>> result4 = starter.execute("  insert     vALues    'cost' =   100.1  ,'lastName'= '  ОшИбка  '   ,  'age'= 10 , 'id'= 4 , 'active'=  true");
            List<Map<String, Object>> result5 = starter.execute("  insert   valuEs    '   cost   ' =   null  ,'lastName   '=   'Селезнев'   , 'active'=  true, 'age  '= 123 , 'id'= 5");
            System.out.println("Insert result: " + result5);
            // update test
//            List<Map<String, Object>> result6 = starter.execute(" update   values 'lastName' =    ' АпдейтИмя ', '   active'= true  , 'cost  '=   88.8 ");
//            List<Map<String, Object>> result7 = starter.execute("update values 'lastName' =    ' АпдейтИмя ', '   active'= true  , 'cost  '=   88.8  wHerE ' age  ' >   50 and 'id' > 2");
            List<Map<String, Object>> result12 = starter.execute("update values 'lastName' =    ' АпдейтИмя ', '   active'= null  wHerE ' age  ' >   50 and 'id' > 2");
            System.out.println("Update result: " + result5);
            // delete test
//            List<Map<String,Object>> result8 = starter.execute("delete");
//            List<Map<String,Object>> result9 = starter.execute("delete where 'age '  > 50 or ' active' != true");
//            System.out.println("Delete result: " + result1);
            // select test
//            List<Map<String,Object>> result10 = starter.execute("select");
//            List<Map<String,Object>> result11 = starter.execute("select where 'lastName' = 'Селезнев'");
//            System.out.println("Select result: " + result1);
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }
}
