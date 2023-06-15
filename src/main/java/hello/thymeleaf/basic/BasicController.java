package hello.thymeleaf.basic;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/basic")
public class BasicController {

    @GetMapping("text-basic")
    public String textBasic(Model model){
        model.addAttribute("data","hello-spring");
        return "basic/text-basic";
    }

    /**
    escaping characters are used to transform <> and other special characters
    that are reserved for HTML into a different character that can be represented
    as the same upon the finished HTML. i.e) ">" --> &gt
    this is typically recommended to be used as the unescaped version can lead to
    possible malicious inputs by the users being allowed to generate unwanted side-effects
    on the server side render
     */
    @GetMapping("/text-unescaped")
    public String textUnescaped(Model model) {
        model.addAttribute("data", "Hello <b>Spring!</b>");
        return "basic/text-unescaped";
    }

    /**
     * SpringEL notations
     *
     * Objects
     *
     * user.username : user.getUsername()
     * user['username'] : user.getUsername()
     * user.getUsername() : "
     *
     * List
     * users[0].username : "
     * list.get(0).getUsername() : "
     * users[0]['username'] : "
     * users[0].getUsername() : "
     *
     * Map
     * userMap['userA'].username : "
     * map.get("userA").getUsername() : "
     * userMap['userA']['username'] : "
     * userMap['userA'].getUsername() : "
     *
     * regional variable declaration thymeleaf
     * th:with allows the creation of a regional variable whose scope is within the same tag
     * 있다.
     */
    @GetMapping("/variable")
    public String variable(Model model){

        User userA = new User("userA", 10);
        User userB = new User("userB", 10);

        List<User> list = new ArrayList<>();
        list.add(userA);
        list.add(userB);

        Map<String, User> map = new HashMap<>();
        map.put("userA", userA);
        map.put("userB", userB);

        model.addAttribute("user", userA);
        model.addAttribute("users", list);
        model.addAttribute("userMap", map);

        return "basic/variable";
    }

    /** this section encountered an update after it was finished
     *
     * thymeleaf used to provide these basic Thymeleaf objects
     * <-- as of spring boot 3.0 the following are discontinued -->
     * ${#request} - deprecated from spring boot v3.0
     * ${#response} - deprecated from spring boot v3.0
     * ${#session} - deprecated from spring boot v3.0
     * ${#servletContext} - deprecated from spring boot v3.0
     *
     * <-- this is still provided -->
     * ${#locale}
     *
     * to use the above discontinued objects from spring boot 3.0 onwards, they
     * have to be manually added to a model object to be used
     *
     */
    @GetMapping("/basic-objects")
    public String basicObjects(Model model, HttpServletRequest request,
                               HttpServletResponse response, HttpSession session) {
        session.setAttribute("sessionData", "Hello Session");
        model.addAttribute("request", request);
        model.addAttribute("response", response);
        model.addAttribute("servletContext", request.getServletContext());
        return "basic/basic-objects";
    }

    /**
     * to use Java 8 dateTime objects , thymeleaf needs "thymeleaf-extras-java8time"
     * as an extra dependency
     * which Spring boot automatically resolves
     *
     * Thymeleaf utility objects
     * https://www.thymeleaf.org/doc/tutorials/3.0/usingthymeleaf.html#expression-utilityobjects
     * examples
     * https://www.thymeleaf.org/doc/tutorials/3.0/usingthymeleaf.html#appendix-b-expressionutility-objects
     *
     *
     * refer to the above links should the need arises, datetime added separately as it was not found
     * in the manual pages
     */
    @GetMapping("/date")
    public String date(Model model) {
        model.addAttribute("localDateTime", LocalDateTime.now());
        return "basic/date";
    }

    /**
     * to use links within thymeleaf, follow the following format
     * @{...}
     *
     * simple url - absolute
     * i.e) @{/hello} -> /hello
     *
     * simple url - relative
     * i.e) @{hello} -> hello
     *
     * QueryParameter
     * @{/hello(param1=${param1}, param2=${param2})}
     * /hello?param1=data1&param2=data2
     *
     * variables in () will be regarded as a query parameter
     *
     * PathVariable
     * @{/hello/{param1}/{param2}(param1=${param1}, param2=${param2})}
     * /hello/data1/data2
     * if there is a variable along the URL path, variables within () will
     * be regarded as a PathVariable
     *
     * QueryParam && PathVariable
     * @{/hello/{param1}(param1=${param1}, param2=${param2})}
     * /hello/data1?param2=data2
     * both QueryParams and PathVariable can be used simultaneously
     *
     */
    @GetMapping("/link")
    public String link(Model model) {
        model.addAttribute("param1", "data1");
        model.addAttribute("param2", "data2");
        return "basic/link";
    }

    /**
     *
     * literals are defined as hard coded values
     * literals are by definition, needed to be encapsulated by ' (single quotation marks)
     * singular word, not separated by any spaces can omit '
     * for values separated by spaces, '...' is needed
     *
     * or one can use  (Literal substitutions) | ... | to omit ' '
     * <span th:text="|hello ${data}|">
     */
    @GetMapping("/literal")
    public String literal(Model model) {
        model.addAttribute("data", "'Spring !'");
        return "basic/literal";
    }

    /**
     *
     * comparators (comparison operators) -> beware of HTML entity usage
     * > (gt), < (lt), >= (ge), <= (le), ! (not), == (eq), != (neq, ne)
     * conditionals : similar to java
     * Elvis operations : simplified conditionals
     * No-Operation: if _ , then will act as if thymeleaf not working,
     * allows for base HTML to be used
     */
    @GetMapping("/operation")
    public String operation(Model model) {
        model.addAttribute("nullData", null);
        model.addAttribute("data", "Spring!");
        return "basic/operation";
    }

    /**
     *
     * attributes
     * th: * = ""
     * whatever comes in * replaces existing attribute value
     * if such an attribute doesnt exist, creates a new attribute
     * <input type="text" name="mock" th:name="userA" />
     * after thymeleaf rendering -> <input type="text" name="userA" />
     *
     * addition of attribute
     * th:attrappend : 속성 값의 뒤에 값을 추가한다.
     * th:attrprepend : 속성 값의 앞에 값을 추가한다.
     * th:classappend : class 속성에 자연스럽게 추가한다.
     *
     * checked
     * <input type="checkbox" name="active" checked="false" />
     * in HTML, existence of attribute 'checked' automatically checks the checkbox entity
     * th:checked = " false " thus removes the 'checked' tag automatically
     * <input type="checkbox" name="active" th:checked="false" />
     * after thymeleaf rendering: <input type="checkbox" name="active" />
     */

    @GetMapping("/attribute")
    public String attribute() {
        return "basic/attribute";
    }

    /**
     *
     * forEach
     *
     * <tr th:each="user : ${users}">
     * similar to Java
     * for(User user : users)
     *
     * th:each works for any Java objects that implemented
     * Java.util.Iterable , java.util.Enumeration
     * for classes that implemented Map, the value in the enhanced
     * for loop will be JAVA Map.Entry
     *
     * repeat status
     * <tr th:each="user, userStat : ${users}">
     * second parameter stores the status of the for loop
     * second parameter can be omitted, will be automatically named
     * (repeatValueName) + Stat;
     *
     * 반복 상태 유지 기능
     * index : 0 - index count
     * count : 1 - index count
     * size : size of Collection
     * even , odd : ( boolean )
     * first , last :( boolean )
     * current : curr Object in repeat
     */

    @GetMapping("/each")
    public String each(Model model) {
        addUsers(model);
        return "basic/each";
    }

    /**
     *
     * if, unless
     * does not render the entire html tag if the th:if == false
     * <span th:text="'미성년자'" th:if="${user.age lt 20}"></span>
     * switch
     * same as JAVA switch
     * "*" is used congruently as default:
     *
     */
    @GetMapping("/condition")
    public String condition(Model model) {
        addUsers(model);
        return "basic/condition";
    }

    /**
     * types
     * 1. standard HTML comment
     *
     * 2. thymeleaf parser comment
     * thymeleaf removes this upon render
     *
     * 3. thymeleaf prototype comment
     * similar to HTML comment(is actually one) , thus does not render on HTML view
     * but it renders upon thymeleaf processing
     */
    @GetMapping("/comments")
    public String comments(Model model) {
        model.addAttribute("data", "Spring!");
        return "basic/comments";
    }

    /**
     * the only original thymeleaf tag (no HTML equivalent)
     * automatically removed upon thymeleaf render.
     * used to create an indent block of where all tags within will
     * be affected by the for loop that accompanies the block tag
     */
    @GetMapping("/block")
    public String block(Model model) {
        addUsers(model);
        return "basic/block";
    }

    @GetMapping("/javascript")
    public String javascript(Model model) {
        model.addAttribute("user", new User("userA", 10));
        addUsers(model);
        return "basic/javascript";
    }



    @Component("helloBean")
    static class HelloBean {
        public String hello(String data) {
            return "Hello " + data;
        }
    }


    @Data
    @AllArgsConstructor
    private static class User {
        private String username;
        private int age;
    }

    private static void addUsers(Model model) {
        List<User> list = new ArrayList<>();
        list.add(new User("userA", 10));
        list.add(new User("userB", 20));
        list.add(new User("userC", 30));
        model.addAttribute("users", list);
    }


}
