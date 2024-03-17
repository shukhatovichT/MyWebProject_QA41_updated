package tests;

import config.BaseTest;
import helpers.*;
import io.qameta.allure.Allure;
import model.Contact;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import pages.*;

import java.io.IOException;

public class PhoneBookTest extends BaseTest {

    @Test(description = "test that checks the functionality of registering an already existing user")
    public void registrationOfAnAlreadyRegisteredUser(@Optional("chrome") String browser){
        MainPage mainPage = new MainPage(getDriver());

        Allure.step("open login page");
        LoginPage loginPage = mainPage.openTopMenu(TopMenuItem.LOGIN.toString());
        Allure.step("registration for new user");
        loginPage.fillEmailField("testemail@mail.com")
                .fillPasswordField("12345Test!")
                .clickByRegistartionButton();

        Allure.step("sign out from the user cabinet");
        ContactsPage contactsPage = new ContactsPage(getDriver());
        contactsPage.signOutButton.click();

        Allure.step("registration for already existing user");
        String expectedString = "User already exist";
        Alert alert= loginPage.fillEmailField("testemail@mail.com")
                .fillPasswordField("12345Test!")
                .clickByRegistartionButton();
        boolean isAlertHandled = AlertHandler.handleAlert(alert, expectedString);
        Assert.assertTrue(isAlertHandled);
    }
    @Test(description = "the test checks creating new contact and deleting this contact")
    public void createAndDeleteContact(@Optional("chrome") String browser){

        MainPage mainPage = new MainPage(getDriver());
        // Login for existing user
        Allure.step("open login page");
        LoginPage loginPage = mainPage.openTopMenu(TopMenuItem.LOGIN.toString());
        Allure.step("login for existing user");
        loginPage.fillEmailField(PropertiesReader.getProperty("existingUserEmail"))
                .fillPasswordField(PropertiesReader.getProperty("existingUserPassword"));
        loginPage.clickByLoginButton();

        Allure.step("create new contact");
        MainPage.openTopMenu(TopMenuItem.ADD.toString());
        AddPage addPage = new AddPage(getDriver());
        Contact newContact = new Contact(NameAndLastNameGenerator.generateName(),
                NameAndLastNameGenerator.generateLastName(),
                PhoneNumberGenerator.generatePhoneNumber(),
                EmailGenerator.generateEmail(10,5,3),
                AddressGenerator.generateAddress(),
                "new description");
        newContact.toString();
        addPage.fillFormAndSave(newContact);


        ContactsPage contactsPage = new ContactsPage(getDriver());
     //   Assert.assertTrue(contactsPage.getDataFromContactList(newContact));

    }

    @Test(description = "the test checks creating new contact for new user after registration")
    public void createContactForUserAfterRegistration(@Optional("chrome") String browser){
        Allure.description("Make registration for new user. Create contact.");
        MainPage mainPage = new MainPage(getDriver());

        // Registration for new user
        Allure.step("Click by Login button");
        LoginPage loginPage = mainPage.openTopMenu(TopMenuItem.LOGIN.toString());
        loginPage.fillEmailField(EmailGenerator.generateEmail(8,4,3)).fillPasswordField(PasswordStringGenerator.generateString());
        Allure.step("Click by Reg button");
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
        Allure.step("fill form for new contact and save");
        addPage.fillFormAndSave(contact);

        // Check if field information about contact exists
        Allure.step("get data from contact list and check that created contact is the same as existed contact in the list");
        ContactsPage contactsPage = new ContactsPage(getDriver());
        contactsPage.getDataFromContactList(contact);


    }



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
    public void createContactForRegistratedUser(@Optional("chrome") String browser) throws InterruptedException {
        Allure.description("User already exists. Login and add contact!");
        MainPage mainPage = new MainPage(getDriver());
        Allure.step("Step 1");

        // Login for existing user
        LoginPage loginPage = mainPage.openTopMenu(TopMenuItem.LOGIN.toString());
        Allure.step("Step 2");
        loginPage.fillEmailField(PropertiesReader.getProperty("existingUserEmail"))
                .fillPasswordField(PropertiesReader.getProperty("existingUserPassword"));
        loginPage.clickByLoginButton();

        Allure.step("Step 3");
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
        Assert.assertTrue(contactsPage.getDataFromContactList(contact));
        TakeScreen.takeScreenshot("screen");

        Thread.sleep(3000);
    }

    @Test
    public void deleteContactApproachTwo() throws IOException {
        String filename = "contactDataFile.ser";
        MainPage mainPage = new MainPage(getDriver());
        LoginPage lpage = mainPage.openTopMenu(TopMenuItem.LOGIN.toString());
        lpage.fillEmailField(PropertiesReader.getProperty("existingUserEmail"))
                .fillPasswordField(PropertiesReader.getProperty("existingUserPassword"))
                .clickByLoginButton();
        MainPage.openTopMenu(TopMenuItem.ADD.toString());
        AddPage addPage = new AddPage(getDriver());
        Contact newContact = new Contact(NameAndLastNameGenerator.generateName(),NameAndLastNameGenerator.generateLastName(),
                PhoneNumberGenerator.generatePhoneNumber(),
                EmailGenerator.generateEmail(10,5,3),
                AddressGenerator.generateAddress(), "Test description");
        addPage.fillFormAndSave(newContact);
        Contact.serializeContact(newContact, filename);
        ContactsPage contactsPage = new ContactsPage(getDriver());
        Contact deserContact = Contact.desiarializeContact(filename);
        Assert.assertNotEquals(contactsPage.deleteContactByPhoneNumberOrName(deserContact.getPhone()),
                contactsPage.getContactsListSize());

    }
}
