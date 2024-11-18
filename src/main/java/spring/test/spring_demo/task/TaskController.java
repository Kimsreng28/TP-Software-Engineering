package spring.test.spring_demo.task;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/TP04")
public class TaskController {
    @GetMapping("/task01")
    public String getTask01(Model model) {
        return "task01";
    }

    @GetMapping("/task02")
    public String getTask02(Model model) {
        return "Task02";
    }

    @GetMapping("/task03")
    public String getTask03(Model model) {
        return "Task03";
    }

    @GetMapping("/task04")
    public String getTask04(Model model) {
        List<Update> updates = new ArrayList<>();
        updates.add(new Update("#007bff", "username0", "Donec id elit non mi porta gravida at eget metus. Fusce dapibus, tellus ac cursus commodo."));
        updates.add(new Update("#e83e8c", "username1",  "Hello"));
        updates.add(new Update("#6f42c1", "username2",  "Word"));
        model.addAttribute("updates", updates);
        return "Task04";
    }

    @GetMapping("/task05")
    public String getTask05(Model model) {
        return "Task05";
    }
    
    @PostMapping("/task05")
    public String getTask05(@RequestParam("username") String username, @RequestParam("password") String password, Model model) {
        if("Kimsreng".equals(username) && "123456".equals(password)){
            List<Update> updates = new ArrayList<>();
            updates.add(new Update("#007bff", "username0", "Donec id elit non mi porta gravida at eget metus. Fusce dapibus, tellus ac cursus commodo."));
            updates.add(new Update("#e83e8c", "username1",  "Hello"));
            updates.add(new Update("#6f42c1", "username2",  "Word"));
            model.addAttribute("updates", updates);
            return "Task04";
        }
        else{
            model.addAttribute("error", "Wrong username or password");
            return "task05";
        }
    }
}
