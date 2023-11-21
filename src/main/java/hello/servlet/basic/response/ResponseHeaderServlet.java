package hello.servlet.basic.response;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "responseHeaderServlet", urlPatterns = "/response-header")
public class ResponseHeaderServlet extends HttpServlet {
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // [status-line]
        response.setStatus(HttpServletResponse.SC_OK); // http 응답 코드 넣기
        // [response-headers]
        response.setHeader("Content-Type", "text/plain;charset=utf-8"); // 인코딩 설정 안하면 한글깨짐 -> utf-8로 지정하기
        response.setHeader("Cache-Control", "no-cache no-store must-revalidate"); // 캐시를 완전히 무효화
        response.setHeader("Pragma", "no-cache"); // 이것 역시 캐시 무효화
        response.setHeader("My-Header", "hello! this is custom header"); // 헤더를 임의로 만들 수 있다.
        // [header 편의 메서드]
//        content(response);
        // [쿠키 편의 메서드]
//        cookie(response);
        // [리다이렉트 편의 메서드]
//        redirect(response);

        PrintWriter writer = response.getWriter();
        writer.println("ok");
    }

    private void content(HttpServletResponse response) {
        //Content-Type: text/plain;charset=utf-8
        //Content-Length: 2
        //response.setHeader("Content-Type", "text/plain;charset=utf-8");
        response.setContentType("text/plain");
        response.setCharacterEncoding("utf-8");
        //response.setContentLength(2); //(생략시 자동 생성) -> 보통 생략
    }

    private void cookie(HttpServletResponse response) {
        //Set-Cookie: myCookie=good; Max-Age=600;
        //response.setHeader("Set-Cookie", "myCookie=good; Max-Age=600");
        Cookie cookie = new Cookie("myCookie", "good");
        cookie.setMaxAge(600); //600초
        response.addCookie(cookie);
    }

    private void redirect(HttpServletResponse response) throws IOException {
        //Status Code 302
        //Location: /basic/hello-form.html
        //response.setStatus(HttpServletResponse.SC_FOUND); //302
        //response.setHeader("Location", "/basic/hello-form.html");
        response.sendRedirect("/basic/hello-form.html");
    }
}
