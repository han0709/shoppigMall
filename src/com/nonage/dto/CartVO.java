package com.nonage.dto;

import java.sql.Timestamp;

public class CartVO {
  private int cseq; //장바구니 번호
  private String id; //구매자 ID
  private int pseq; // 상품번호
  private String mname;//MEMBER 테이블에 있는 멤버 이름
  private String pname;//PRODUCT 테이블에 있는 제품이름
  private int quantity; //수량
  private int price2; //판매가격
  private Timestamp indate; //등록일
  
  public int getPrice2() {
    return price2;
  }
  public void setPrice2(int price2) {
    this.price2 = price2;
  }
  
  public int getCseq() {
    return cseq;
  }
  public void setCseq(int cseq) {
    this.cseq = cseq;
  }
  public String getId() {
    return id;
  }
  public void setId(String id) {
    this.id = id;
  }
  public int getPseq() {
    return pseq;
  }
  public void setPseq(int pseq) {
    this.pseq = pseq;
  }
  public String getMname() {
    return mname;
  }
  public void setMname(String mname) {
    this.mname = mname;
  }
  public String getPname() {
    return pname;
  }
  public void setPname(String pname) {
    this.pname = pname;
  }
  public int getQuantity() {
    return quantity;
  }
  public void setQuantity(int quantity) {
    this.quantity = quantity;
  }
  public Timestamp getIndate() {
    return indate;
  }
  public void setIndate(Timestamp indate) {
    this.indate = indate;
  }  
}
