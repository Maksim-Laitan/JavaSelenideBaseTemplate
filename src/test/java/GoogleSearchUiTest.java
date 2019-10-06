import base.BaseUiTestClass;
import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class GoogleSearchUiTest extends BaseUiTestClass {

    @Test
    @DisplayName("Google Search page simple test")
    void googleSearchTest() {
        open("https://google.com");
        $(By.name("q")).val("Google").pressEnter();
        $$("#res .g").shouldHave(CollectionCondition.sizeGreaterThan(5));
        $("#res .g", 2).shouldHave(Condition.text("Google Accounts: Sign in"));
        System.out.println("=====================");
        System.out.println("The Google search test is finished");
        System.out.println("=====================");
    }
}
