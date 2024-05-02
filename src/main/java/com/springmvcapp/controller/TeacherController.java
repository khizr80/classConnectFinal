package com.springmvcapp.controller;
import com.springmvcapp.model.Course;
import com.springmvcapp.model.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
@Controller
public class TeacherController {
    private final Teacher s;


    public TeacherController(Teacher s) {
        this.s = s;
    }
    String username;
    String courseid;
    @PostMapping("/viewCourseTeacher")
    public String viewCourse(Model model, HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("username".equals(cookie.getName())) {
                    username = cookie.getValue();
                    break;
                }
            }
        }

        List<Course> courses = s.getCoursesByTeacherUsername(username);
        model.addAttribute("courses", courses);
        return "courseTeacher"; // Ensure this redirects or forwards to the appropriate view
    }
    @PostMapping("/submitCourseTeacher")
    public String handleCourseSubmission(@RequestParam("courseId") String courseId,Model model, HttpServletResponse response) {
        model.addAttribute("courseId", courseId);
        System.out.println(courseId);
        createCookie(response,courseId);
        courseid=courseId;
        return "courseDetailsTeacher"; // Redirect or forward to a view displaying details of the submitted course
    }
    @PostMapping("/viewStreamMessagesTeacher")
    public String viewStreamMessages(@RequestParam("courseId") String courseId,Model model) {
        List<Map<String, Object>> messages=s.getStreamMessages(username,courseId,"c");
        model.addAttribute("messages", messages); // Add messages to the model
        return "streamMessages"; // View name or redirection
    }
    @PostMapping("/sendStreamMessageTeacher")
    public String sendStreamMessage(@RequestParam("courseId") String courseId,Model model) {
        model.addAttribute("courseId", courseId);
        return "messageTeacher"; // Redirect after sending message
    }
    @PostMapping("/sendStreamMessageTeacher2")
    public String sendStreamMessage(@RequestParam("courseId") String courseId, @RequestParam("messageText") String messageText, Model model,@RequestParam("action") String action) {
            return s.insertMessage(username,courseId,"c",messageText);
    }
    @PostMapping("/sendStudentMessages2")
    public String viewTeacherMessages(@RequestParam("courseId") String courseId,Model model) {
        String g="";

        System.out.println(g);
        List<Map<String, Object>> messages=s.getMessagesBySenderReceiverAndRole(username,g,"t");
        model.addAttribute("messages", messages); // Add messages to the model
        return "streamMessages"; // View name or redirection
    }
    @PostMapping("/sendStudentMessages")
    public String sendStudentMessages(@RequestParam("courseId") String courseId,Model model) {
        model.addAttribute("courseId", courseId);
        return "studentSendMessages"; // View name or redirection
    }
    @PostMapping("/viewStudentMessages")
    public String viewStudentMessages(@RequestParam("courseId") String courseId,Model model) {
        model.addAttribute("courseId", courseId);
        return "studentViewMessages"; // View name or redirection
    }
    @PostMapping("/viewMessages")
    public String viewMessages(@RequestParam("courseId") String courseId,
                               @RequestParam("username") String user,
                               Model model) {
        List<Map<String, Object>> messages= s.getMessagesBySenderReceiverAndRole(username,user,"t");
        model.addAttribute("messages", messages); // Add messages to the model
        return "streamMessages"; // This should be a .html file in your resources/templates directory
    }
    @PostMapping("/sendMessages")
    public String sendMessages(@RequestParam("courseId") String courseId,
                               @RequestParam("username") String user,
                               @RequestParam("messageText") String messageText,
                               Model model) {
            return s.insertMessage(username,user,"t",messageText);
    }
    @PostMapping("/uploadMarks")
    public String uploadMarks(@RequestParam("courseId") String courseId,
                               Model model) {
        List<String> getstudentsofcourse=s.getDistinctEvaluationNames(courseId,username);
        model.addAttribute("evaluationNames", getstudentsofcourse);
        model.addAttribute("courseId", courseId);

        return "evaluationName";
    }

    @PostMapping("/setMarksEvaluation")
    public String setMarksEvaluation(@RequestParam("courseId") String courseId,
                               Model model) {
        model.addAttribute("courseId", courseId);
       return "SetMarkEvaluation";

    }
    @PostMapping("/processMarks")
    public String setMarksEvaluate(@RequestParam("courseId") String courseId, @RequestParam("evaluationName") String evaluationName,
                                     Model model) {
        model.addAttribute("courseId", courseId);
        model.addAttribute("evaluationName", evaluationName); // Adding evaluation name to the model
        List<Map<String, Object>> marksList=s.getMarksByCourseAndEvaluation(courseId,evaluationName,username);
        model.addAttribute("marksList", marksList); // Corrected line
        return "uploadMarksForm";
    }
    @PostMapping("/submitEvaluation")
    public String submitEvaluation(@RequestParam("courseId") String courseId,
                                   @RequestParam("evaluationName") String evaluationName,
                                   @RequestParam("weightage") String weightage,
                                   @RequestParam("totalMarks") String totalMarks,
                                     Model model)
    {

        List<String> getstudentsofcourse = s.getStudentsByCourseAndTeacher(courseId,username);
        s.insertMarks(getstudentsofcourse,evaluationName,weightage,totalMarks,username,courseId,"0");
        return "success";
    }

    @GetMapping("/markAttendance")
    public String markAttendance( Model model) {
        List<Map<String, Object>> students = s.getStudentsByCourseId(courseid); // Fetch students enrolled in the course
        model.addAttribute("students", students);
        model.addAttribute("courseId", courseid);
        return "markAttendance"; // Display the mark attendance form
    }

    @PostMapping("/saveAttendance")
    public String saveAttendance(@RequestParam("courseId") String courseId,
                                 @RequestParam("attendance") String[] attendance,
                                 Model model) {

        for (String entry : attendance) {
            String[] parts = entry.split(":");
            String Username = parts[0];
            String value = parts[1];
            LocalDate today = LocalDate.now();
            s.insertAttendance(Username,username,courseId,value,today);
        }
        model.addAttribute("courseId", courseId);

        return "redirect:/markAttendance";
    }
    @PostMapping("/submitMarks")
    public String submitMarks(@RequestParam("courseId") String courseId,
                              @RequestParam("evaluationName") String evaluationName,
                              @RequestParam Map<String, String> obtainedMarksMap) {
        List<String> studentUsernames = s.getStudentsByCourseAndTeacher(courseId, username);
        List<String> y = new ArrayList<>();
        for (Map.Entry<String, String> entry : obtainedMarksMap.entrySet()) {
            String studentUsername = entry.getKey();
            String obtainedMarks = entry.getValue();
            y.add(obtainedMarks);
        }

        if (y.size() >= 2) {
            y.remove(0); // Remove first element
            y.remove(0); // Remove second element (after removal, what was at index 2 becomes the new index 0)
        } else if (y.size() == 1) {
            y.remove(0); // Remove the first element if only one element is present
        }
        for (int i = 0; i < studentUsernames.size(); i++) {
            String studentUsername = studentUsernames.get(i);
            if (i < y.size()) {
                String obtainedMarks = y.get(i);
                s.saveObtainedMarks(courseId,evaluationName, studentUsername, obtainedMarks);
            }
        }
        return "success";
    }
    @PostMapping("/viewClassProgress")
    public String viewClassProgress(@RequestParam("courseId") String courseId, Model model) {
        List<Map<String, Object>> progressList = s.getClassProgress(courseId);
        model.addAttribute("progressList", progressList);
        return "classProgress";
    }
    private void createCookie(HttpServletResponse response, String username) {
        Cookie usernameCookie = new Cookie("courseId", username);
        usernameCookie.setMaxAge(24 * 60 * 60); // 1 day
        response.addCookie(usernameCookie);
    }
}
