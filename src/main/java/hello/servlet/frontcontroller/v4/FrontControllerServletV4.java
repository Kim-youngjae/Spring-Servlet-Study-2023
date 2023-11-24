package hello.servlet.frontcontroller.v4;

import hello.servlet.frontcontroller.ModelView;
import hello.servlet.frontcontroller.MyView;
import hello.servlet.frontcontroller.v4.controller.MemberFormControllerV4;
import hello.servlet.frontcontroller.v4.controller.MemberListControllerV4;
import hello.servlet.frontcontroller.v4.controller.MemberSaveControllerV4;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "frontControllerServletV4", urlPatterns = "/front-controller/v4/*")
public class FrontControllerServletV4 extends HttpServlet {
    Map<String, ControllerV4> controllerMap = new HashMap<>();

    public FrontControllerServletV4() { // 경로와 해당 경로로 접근했을 때 처리할 컨트롤러를 매핑하여 초기화
        controllerMap.put("/front-controller/v4/members/new-form", new MemberFormControllerV4());
        controllerMap.put("/front-controller/v4/members/save", new MemberSaveControllerV4());
        controllerMap.put("/front-controller/v4/members", new MemberListControllerV4());
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 최초에 들어올 때는 서블릿을 통해 들어옴
        String requestURI = request.getRequestURI();
        ControllerV4 controller = controllerMap.get(requestURI); // 해당 물리 주소가 담긴 모델 뷰 정보를 가져 온다
        if(controller == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        Map<String, String> paramMap = createParamMap(request); // process 메서드에 넣기 위한 map 생성
        Map<String, Object> model = new HashMap<>();

        String viewName = controller.process(paramMap, model);

        MyView view = viewResolver(viewName); // 받은 모델 뷰의 뷰 호출을 위해 뷰 리졸버를 호출

        view.render(model, request, response); // 랜더링 -> 뷰가 랜더링되기 위해 모델이 필요함
    }

    private static MyView viewResolver(String viewName) {
        return new MyView("/WEB-INF/views/" + viewName + ".jsp");
    }

    private static Map<String, String> createParamMap(HttpServletRequest request) {
        Map<String, String> paramMap = new HashMap<>();
        request.getParameterNames().asIterator()
                .forEachRemaining(paramName -> paramMap.put(paramName, request.getParameter(paramName)));
        return paramMap;
    }
}
