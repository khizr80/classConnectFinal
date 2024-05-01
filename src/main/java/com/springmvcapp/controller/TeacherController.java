package com.springmvcapp.controller;
import com.springmvcapp.model.Course;

import com.springmvcapp.model.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

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
        System.out.println(courses.get(0));
        return "courseTeacher"; // Ensure this redirects or forwards to the appropriate view
    }
    @PostMapping("/submitCourseTeacher")
    public String handleCourseSubmission(@RequestParam("courseId") String courseId,Model model) {
        model.addAttribute("courseId", courseId);
        return "courseDetailsTeacher"; // Redirect or forward to a view displaying details of the submitted course
    }
    @PostMapping("/viewStreamMessagesTeacher")
    public String viewStreamMessages(@RequestParam("courseId") String courseId,Model model) {
        List<Map<String, Object>> messages=s.getMessagesBySenderReceiverAndRole(username,courseId,"c");
        model.addAttribute("messages", messages); // Add messages to the model
        return "streamMessages"; // View name or redirection
    }
    @PostMapping("/sendStreamMessageTeacher")
    public String sendStreamMessage(@RequestParam("courseId") String courseId,Model model) {
        model.addAttribute("courseId", courseId);
        //String g=s.getTeacherUsernameByCourseId(courseId);
        //model.addAttribute("teacherUsername", g);
        return "messageTeacher"; // Redirect after sending message
    }
    @PostMapping("/sendStreamMessageTeacher2")
    public String sendStreamMessage(@RequestParam("courseId") String courseId,
                                    @RequestParam("messageText") String messageText,
                                    Model model,@RequestParam("action") String action) {

            return s.insertMessage(username,courseId,"c",messageText);

    }
    @PostMapping("/sendStudentMessages2")
    public String viewTeacherMessages(@RequestParam("courseId") String courseId,Model model) {
//        String g=s.getTeacherUsernameByCourseId(courseId);
        String g="";

        System.out.println(g);
        List<Map<String, Object>> messages=s.getMessagesBySenderReceiverAndRole(username,g,"t");
        model.addAttribute("messages", messages); // Add messages to the model
        if (messages != null) {
            for (Map<String, Object> message : messages) {
                System.out.println("Sender: " + message.get("sender"));
                System.out.println("Receiver: " + message.get("receiver"));
                System.out.println("Text: " + message.get("text"));
                System.out.println("Date: " + message.get("date"));
                System.out.println("Role: " + message.get("role"));
                System.out.println("------------------------");
            }
        } else {
            System.out.println("No messages found.");
        }
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

    @PostMapping("/markAttendance")
    public String markAttendance(@RequestParam("courseId") String courseId, Model model) {
        List<Map<String, Object>> students = s.getStudentsByCourseId(courseId); // Fetch students enrolled in the course
        model.addAttribute("students", students);
        model.addAttribute("courseId", courseId);
        return "markAttendance"; // Display the mark attendance form
    }

    @PostMapping("/saveAttendance")
    public String saveAttendance(@RequestParam("courseId") String courseId,
                                 @RequestParam Map<String, String> attendance) {
        List<String> presentStudents = new ArrayList<>();
        List<String> absentStudents = new ArrayList<>();

        // Iterate over the attendance map and separate present and absent students
        for (Map.Entry<String, String> entry : attendance.entrySet()) {
            String studentId = entry.getKey();
            String status = entry.getValue();
            if ("present".equals(status)) {
                presentStudents.add(studentId);
            } else if ("absent".equals(status)) {
                absentStudents.add(studentId);
            }
        }

        // Save attendance in the database
        s.saveAttendance(courseId, presentStudents, absentStudents);
        // Redirect to course details page
        return "redirect:/viewCourseTeacher";
    }
//    //
        //return "success"; // Redirect to a success page or any other appropriate page
    //}
@PostMapping("/submitMarks")
public String submitMarks(@RequestParam("courseId") String courseId,
                          @RequestParam("evaluationName") String evaluationName,
                          @RequestParam Map<String, String> formParams) {

    for (Map.Entry<String, String> entry : formParams.entrySet()) {
        String paramName = entry.getKey();
        String value = entry.getValue();
        System.out.println("Parameter Name: " + paramName + ", Value: " + value);
    }
    return "success";
}


}
