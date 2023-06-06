package com.restful.booker.bookinginfo;

import com.restful.booker.testbase.TestBase;
import com.restful.booker.utils.TestUtils;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import net.serenitybdd.junit.runners.SerenityRunner;
import net.thucydides.core.annotations.Steps;
import net.thucydides.core.annotations.Title;
import net.thucydides.core.annotations.WithTag;
import net.thucydides.core.annotations.WithTags;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashMap;
@RunWith(SerenityRunner.class)
public class BookingCRUDTest extends TestBase {
    static String username ="admin";
    static String password ="password123";
    static String token;
    static String firstname= "Jason"+ TestUtils.randomStr(3);
    static String lastname= "Brown";
    static int totalprice= 852;
    static  boolean depositpaid =true;
    static String additionalneeds = "Access to Beach";
    static int bookingid;
    @Steps
    BookingSteps bookingSteps;
    @WithTags({
            @WithTag("regression"),
            @WithTag("smoke")
    } )
    @Title("This test will generate token")
    @Test
    public void test001(){
        token = bookingSteps.getToken(username,password);
        System.out.println(token);
    }
    @WithTags({
            @WithTag("regression"),
            @WithTag("sanity")
    } )
    @Title("This test will get all booking ids")
    @Test
    public void test002(){
        ValidatableResponse response = bookingSteps.getAllBookingIds();
        response.log().all().statusCode(200);

    }
    @WithTags({
            @WithTag("regression"),
            @WithTag("smoke")
    } )
    @Title("This test will add new booking information to application and verify the booking has been added by bookingid")
    @Test
    public void test003(){
        HashMap<String,Object> bookingdates = new HashMap<String, Object>();
        bookingdates.put("checkin","2018-01-01");
        bookingdates.put("checkout","2019-01-01");
        Response response = bookingSteps.createNewBooking(firstname,lastname,totalprice,depositpaid,bookingdates,additionalneeds);
        bookingid= response.then().log().all().extract().path("bookingid");
        System.out.println(bookingid);

    }
    @WithTags({
            @WithTag("regression"),
            @WithTag("sanity")
    })
    @Title("This test get single booking information by id ")
    @Test
    public void test004(){
        ValidatableResponse response = bookingSteps.getSingleBookingById(bookingid);
        response.log().all().statusCode(200);
        String actualLastName= response.log().all().extract().path("lastname").toString();
        Assert.assertEquals(lastname,actualLastName);
        System.out.println(actualLastName);

    }
    @WithTags({
            @WithTag("regression"),
            @WithTag("smoke")
    })
    @Title("This test will update existing booking information and verify the booking has been updated by booking id ")
    @Test
    public void test005(){
        HashMap<String,Object> bookingdates = new HashMap<String, Object>();
        bookingdates.put("checkin","2018-01-01");
        bookingdates.put("checkout","2019-01-01");
        firstname = firstname+"_upadate";
        totalprice =1600;
        depositpaid=false;
        Response response = bookingSteps.updateBookingById(bookingid,token,firstname,lastname,totalprice,depositpaid,bookingdates,additionalneeds);
        response.then().log().all().statusCode(200);
        int totalprice = response.then().extract().path("totalprice");
        System.out.println(totalprice);

    }
    @WithTags({
            @WithTag("regression"),
            @WithTag("smoke")
    })
    @Title("This test will only partially update booking information to application by bookingid")
    @Test
    public void test006(){
        firstname = "Neha_Patch";
        Response response = bookingSteps.updatePartiallyBookingById(bookingid,token,firstname);
        response.then().log().all().statusCode(200);

    }
    @WithTags({
            @WithTag("regression"),
            @WithTag("smoke")
    })
    @Title("This test will delete booking information in the application")
    @Test
    public void test007(){
    ValidatableResponse response = bookingSteps.deleteBookingById(bookingid,token);
    response.statusCode(201);
    }

}
