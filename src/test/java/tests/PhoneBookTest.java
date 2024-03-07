package tests;

import config.BaseTest;
import helpers.*;
import io.qameta.allure.Allure;
import model.Contact;
import org.openqa.selenium.Alert;
import org.testng.Assert;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import pages.*;

public class PhoneBookTest extends BaseTest {

    @Test(description = "The test checks the empty field warning declaration.")
    @Parameters("browser")
    public void registrationWithoutPassword (@Optional("chrome") String browser) throws InterruptedException {
        Allure.description("User already exist. Login and add contact.!");

        MainPage mainPage = new MainPage(getDriver());
        Allure.step("Click by Login button");
        LoginPage loginPage = mainPage.openTopMenu(TopMenuItem.LOGIN.toString());
        Allure.step("Click by Reg button");
        String expectedString = "Wrong";

        Alert alert= loginPage.fillEmailField("myemail@mail.com").clickByRegistartionButton();
        boolean isAlertHandled = AlertHandler.handleAlert(alert, expectedString);
        Assert.assertTrue(isAlertHandled);
    }

    @Test
    public void createContactForRegistratedUser(@Optional("chrome") String browser){
        MainPage mainPage = new MainPage(getDriver());

        // Registration for new user
        LoginPage loginPage = mainPage.openTopMenu(TopMenuItem.LOGIN.toString());
        loginPage.fillEmailField(EmailGenerator.generateEmail(8,4,3)).fillPasswordField(PasswordStringGenerator.generateString());
        loginPage.clickByRegistartionButton();

        // Open 'ADD' page
        AddPage addPage = mainPage.openTopMenu(TopMenuItem.ADD.toString());

        // Create a Contact object
        Contact contact = new Contact();

        // Set contact details
        contact.setName(NameAndLastNameGenerator.generateName());
        contact.setLastName(NameAndLastNameGenerator.generateLastName());
        contact.setPhone(PhoneNumberGenerator.generatePhoneNumber());
        contact.setEmail(EmailGenerator.generateEmail(8,4,4));
        contact.setAddress(AddressGenerator.generateAddress());
        contact.setDescription("This is a test contact");

        // Fill fields for contact and save
        addPage.fillFormAndSave(contact);

        // Check if field information about contact exists
        ContactsPage contactsPage = new ContactsPage(getDriver());
        contactsPage.getDataFromContactList(contact);


    }
}
