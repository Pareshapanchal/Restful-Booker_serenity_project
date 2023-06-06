package com.restful.booker;

import com.restful.booker.bookinginfo.BookingSteps;
import com.restful.booker.testbase.TestBase;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import net.thucydides.core.annotations.Steps;
import net.thucydides.core.annotations.Title;
import org.junit.Test;

import java.util.HashMap;

//@UseTestDataFrom("src/test/java/resources/testdata/bookinginfo.csv")
//@RunWith(SerenityParameterizedRunner.class)
public class CreateBookingDataDrivenTest extends TestBase {
    private String username = "admin";
    private String password = "password123";
    private String token;
    private String firstname;
    private String lastname;
    private int totalprice;
    private boolean depositpaid;
    private String additionalneeds;
    private String checkin;
    private String checkout;
    private int bookingid;
    @Steps
    BookingSteps bookingSteps;

    @Title("This test will generate token")
    @Test
    public void test001() {
        token = bookingSteps.getToken(username, password);
        System.out.println(token);
    }
    @Title("This test will add new booking information to application and verify the booking has been added by bookingid")
    @Test
    public void test002(){
        HashMap<String,Object> bookingdates = new HashMap<String, Object>();
        bookingdates.put("checkin",checkin);
        bookingdates.put("checkout",checkout);
        Response response = bookingSteps.createNewBooking(firstname,lastname,totalprice,depositpaid,bookingdates,additionalneeds);
        bookingid= response.then().log().all().extract().path("bookingid");
        System.out.println(bookingid);

    }
    @Title("Get created record by it's booking id ")
    @Test

    public void test003(){
        ValidatableResponse response= bookingSteps.getSingleBookingById(bookingid);
        response.log().all().statusCode(200);

    }
    @Title("This test will delete booking information in the application")
    @Test
    public void test004(){
        ValidatableResponse response = bookingSteps.deleteBookingById(bookingid,token);
        response.statusCode(201);
    }


}