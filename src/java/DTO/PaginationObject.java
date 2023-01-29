/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DTO;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author PQT2212
 * @param <T>
 */
public class PaginationObject<T> {

    private static int numberOfRowEachPage = 12;

    public static int getNumberOfRowEachPage() {
        return numberOfRowEachPage;
    }

    public static void setNumberOfRowEachPage(int numberOfRowEachPage) {
        PaginationObject.numberOfRowEachPage = numberOfRowEachPage;
    }

    public List<T> getPageOfResult(List<T> objects, int page, int number) {
        List<T> list = new ArrayList<>();
        if(page > Math.ceil(((double)objects.size()) / number)){
            return null;
        }
        for (int i = ((page - 1) * number); i < (number*page); i++) {
            if(objects.size()==i){
                break;
            }else{
               list.add(objects.get(i)); 
            }           
        }
        return list;
    }

    public int getTotalPageOfResult(List<T> objects, int number) {
        double total = objects.size();
        return (int) Math.ceil(total / number);
    }
}
