package main.model;

import java.util.Date;

public class Reservation {
    private int id;
    private int roomId;
    private String customerName;
    private String customerSurname;
    private String customerIdentityNo;
    private Date checkinDate;
    private Date checkoutDate;
    private double totalPrice;

    public Reservation(int id, int roomId, String customerName, String customerSurname, String customerIdentityNo, Date checkinDate, Date checkoutDate, double totalPrice) {
        this.id = id;
        this.roomId = roomId;
        this.customerName = customerName;
        this.customerSurname = customerSurname;
        this.customerIdentityNo = customerIdentityNo;
        this.checkinDate = checkinDate;
        this.checkoutDate = checkoutDate;
        this.totalPrice = totalPrice;
    }

    public Reservation(){}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerSurname() {
        return customerSurname;
    }

    public void setCustomerSurname(String customerSurname) {
        this.customerSurname = customerSurname;
    }

    public String getCustomerIdentityNo() {
        return customerIdentityNo;
    }

    public void setCustomerIdentityNo(String customerIdentityNo) {
        this.customerIdentityNo = customerIdentityNo;
    }

    public Date getCheckinDate() {
        return checkinDate;
    }

    public void setCheckinDate(Date checkinDate) {
        this.checkinDate = checkinDate;
    }

    public Date getCheckoutDate() {
        return checkoutDate;
    }

    public void setCheckoutDate(Date checkoutDate) {
        this.checkoutDate = checkoutDate;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "id=" + id +
                ", roomId=" + roomId +
                ", customerName='" + customerName + '\'' +
                ", customerSurname='" + customerSurname + '\'' +
                ", customerIdentityNo='" + customerIdentityNo + '\'' +
                ", checkinDate=" + checkinDate +
                ", checkoutDate=" + checkoutDate +
                ", totalPrice=" + totalPrice +
                '}';
    }
}
