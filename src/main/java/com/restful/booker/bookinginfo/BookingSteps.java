package com.restful.booker.bookinginfo;

import com.restful.booker.constants.EndPoints;
import com.restful.booker.model.AuthPojo;
import com.restful.booker.model.BookingPojo;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import net.serenitybdd.rest.SerenityRest;
import net.thucydides.core.annotations.Step;

import java.util.HashMap;

public class BookingSteps {
    @Step("Get token username :{0}, password:{1}")
    public String getToken(String username, String password){
        // set variables username and password
        AuthPojo authPojo = AuthPojo.getAuthPojo(username,password);
        //this will generate a token
        Response response = SerenityRest.given().log().all()
                .header("Content-Type","application/json")
                .body(authPojo)// pass the variable in the body
                .when()
                .post("https://restful-booker.herokuapp.com/auth")//create a Authorisation
                .then().extract().response();
        String token= response.jsonPath().get("token").toString();
        return token;
    }
    @Step("Get all booking ids")
    public ValidatableResponse getAllBookingIds(){
        return  SerenityRest.given().log().all()
                .when()
                .get()
                .then();
    }
    @Step("Add new Booking Information firstname :{0},lastname : {1}, totalprice : {2}, depositpaid :{2}, bookingdates :{3},additionneed :{4}")
    public Response createNewBooking(String firstname, String lastname, int totalprice, boolean depositpaid, HashMap<String,Object> bookingdates,String additionalneeds){
        BookingPojo bookingPojo = BookingPojo.getBookingPojo(firstname, lastname, totalprice, depositpaid, bookingdates, additionalneeds);
        return SerenityRest.given().log().all()
                .header("Content-Type","application/json")
                .header("Accept","application/json")
                .body(bookingPojo)
                .when()
                .post();
    }
    @Step("Get single booking information by booking id:{0}")
    public ValidatableResponse getSingleBookingById(int bookingid){
        return SerenityRest.given().log().all()
                .pathParam("bookingid",bookingid)
                .when()
                .get(EndPoints.GET_BOOKING_WITH_ID)
                .then();
    }
    @Step("Update Booking information bookingid : {0}firstname :{1},lastname : {2}, totalprice : {3}, depositpaid :{4}, bookingdates :{5},additionneed :{6}")
    public Response updateBookingById(int bookingid,String token,String firstname, String lastname, int totalprice, boolean depositpaid, HashMap<String,Object> bookingdates,String additionalneeds){
        BookingPojo bookingPojo = BookingPojo.getBookingPojo(firstname, lastname, totalprice, depositpaid, bookingdates, additionalneeds);
        return SerenityRest.given().log().all()
                .header("Content-Type","application/json")
                .header("Accept","application/json")
                .header("Cookie","token="+token)
                .pathParam("bookingid",bookingid)
                .body(bookingPojo)
                .when()
                .put(EndPoints.UPDATE_BOOKING_WITH_ID);
    }
    @Step("Update firstname :{2}, booking:{0}")
    public Response updatePartiallyBookingById(int bookingid, String token, String firstname){
        BookingPojo bookingPojo = BookingPojo.getBookingPojo(firstname);

        return SerenityRest.given().log().all()
                .header("Content-Type","application/json")
                .header("Accept","application/json")
                .header("Cookie","token="+token)
                .pathParam("bookingid",bookingid)
                .body(bookingPojo)
                .when()
                .patch(EndPoints.PARTIALLY_UPDATE_BOOKING_WITH_ID);
    }
    @Step("Delete Booking information by bookingid :{0}")
    public ValidatableResponse deleteBookingById(int bookingid, String token){
        return SerenityRest.given().log().all()
                .header("Content-Type","application/json")
                .header("Accept","application/json")
                .header("Cookie", "token="+token)
                .pathParam("bookingid",bookingid)
                .when()
                .delete(EndPoints.DELETE_BOOKING_WITH_ID)
                .then();
    }


}
