package hello.thymeleaf.basic;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
@Controller
@RequestMapping("/template")
public class TemplateController {

    /**
     * fragment:
     * HTML fragment denoted with a th:fragment tag
     * can be reused in other html rendered through thymeleaf
     *
     * th:insert/ th:replace triggers a fragment call,
     * insert inserts the fragment as a child div
     * replace replaces the current div with the fragment
     *
     * link to fragment must be denoted, prefixed with ~{
     * followed by :: fragment_name
     * although simple forms can be accepted through ""
     *
     * argument parameters can be accepted
     */
    @GetMapping("/fragment")
    public String template() {
        return "template/fragment/fragmentMain";
    }

    /**
     * common_header(~{::title},~{::link}).
     * ::title adds the HTML title tag as param
     * ::link adds all HTML link tag as param
     *
     * result:
     * from a base html layout (base) -> can add details through
     * fragment importing from layoutMain
     */
    @GetMapping("/layout")
    public String layout() {
        return "template/layout/layoutMain";
    }

    /**
     * above concept can be extended to apply layout at the html tag level
     * layout is usually more preferred then fragment for important pages,
     * that need constant management
     *
     * when at html level, a base layout page can be made, with each page containing
     * minimal information(info-dump) that is needed for that page adding on to the base
     * layout page.
     *
     * as the html of the current page can be replaced with the base layout page with the
     * various html tags of the current page before change can be passed on to the
     * base layout page as arguments of th:replace
     */
    @GetMapping("/layoutExtend")
    public String layoutExtends() {
        return "template/layoutExtend/layoutExtendMain";
    }
}