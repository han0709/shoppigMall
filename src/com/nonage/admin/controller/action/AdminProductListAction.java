package com.nonage.admin.controller.action;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.nonage.controller.action.Action;
import com.nonage.dao.ProductDAO;
import com.nonage.dto.ProductVO;

public class AdminProductListAction implements Action {

  @Override
  public void execute(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    String url = "admin/product/productList.jsp";
    
    String key=request.getParameter("key");
    String tpage=request.getParameter("tpage");
    
    //key(검색) 과 tpage의 초기값
    if(key==null){
     key="";
    }    
    if(tpage== null){
      tpage="1"; //현재 페이지 (default 1)                        
    }else if(tpage.equals("")){
       tpage="1";  
    }
    
    //view 페이지로 보냄
    request.setAttribute("key", key);
    request.setAttribute("tpage",tpage);
    
    ProductDAO productDAO = ProductDAO.getInstance();
    
    //해당 페이지부터 5개의 열을 가져옴.
    ArrayList<ProductVO> productList= productDAO.listProduct(Integer.parseInt(tpage), key);
    
    //srt(page아래의 넘버들)를 받아옴.
    String paging=productDAO.pageNumber(Integer.parseInt(tpage), key);
     
    request.setAttribute("productList",productList);
    int n=productList.size();   
    System.out.print("productList.size == = == "+productList.size());
    
    request.setAttribute("productListSize",n); 
    request.setAttribute("paging", paging);    
    request.getRequestDispatcher(url).forward(request, response);
  }
}
