package hello.servlet.frontcontroller.v3;

import hello.servlet.frontcontroller.ModelView;
import hello.servlet.frontcontroller.MyView;
import hello.servlet.frontcontroller.v2.ControllerV2;
import hello.servlet.frontcontroller.v2.controller.MemberFormControllerV2;
import hello.servlet.frontcontroller.v2.controller.MemberListControllerV2;
import hello.servlet.frontcontroller.v2.controller.MemberSaveControllerV2;
import hello.servlet.frontcontroller.v3.controller.MemberFormControllerV3;
import hello.servlet.frontcontroller.v3.controller.MemberListControllerV3;
import hello.servlet.frontcontroller.v3.controller.MemberSaveControllerV3;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "frontControllerServletV3", urlPatterns = "/front-controller/v3/*")
public class FrontControllerServletV3 extends HttpServlet {
    Map<String, ControllerV3> controllerMap = new HashMap<>();

    public FrontControllerServletV3() {
        controllerMap.put("/front-controller/v3/members/new-form", new MemberFormControllerV3());
        controllerMap.put("/front-controller/v3/members/save", new MemberSaveControllerV3());
        controllerMap.put("/front-controller/v3/members", new MemberListControllerV3());
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String requestURI = request.getRequestURI();
        ControllerV3 controller = controllerMap.get(requestURI); // 해당 물리 주소가 담긴 모델 뷰 정보를 가져 온다

        if(controller == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        //  ControllerV3의 경우 paramMap을 넘겨 주어야 한다.
        Map<String, String> paramMap = createParamMap(request); // process 메서드에 넣기 위한 map 생성
        ModelView mv = controller.process(paramMap); // 모델 뷰 객체를 반환 받음
        String viewName = mv.getViewName();
        MyView view = viewResolver(viewName); // 받은 모델 뷰의 뷰 호출을 위해 뷰 리졸버를 호출

        view.render(mv.getModel(), request, response); // 랜더링 -> 뷰가 랜더링되기 위해 모델이 필요함
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
