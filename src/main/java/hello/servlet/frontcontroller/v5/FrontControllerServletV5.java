package hello.servlet.frontcontroller.v5;

import hello.servlet.frontcontroller.ModelView;
import hello.servlet.frontcontroller.MyView;
import hello.servlet.frontcontroller.v3.controller.MemberFormControllerV3;
import hello.servlet.frontcontroller.v3.controller.MemberListControllerV3;
import hello.servlet.frontcontroller.v3.controller.MemberSaveControllerV3;
import hello.servlet.frontcontroller.v4.controller.MemberFormControllerV4;
import hello.servlet.frontcontroller.v4.controller.MemberListControllerV4;
import hello.servlet.frontcontroller.v4.controller.MemberSaveControllerV4;
import hello.servlet.frontcontroller.v5.adapter.ControllerV3HandlerAdapter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(name = "frontControllerServletV5", urlPatterns = "/front-controller/v5/*")
public class FrontControllerServletV5 extends HttpServlet {
    private final Map<String, Object> handelerMappingMap = new HashMap<>();
    private final List<MyHandlerAdapter> handlerAdapters = new ArrayList<>();

    public FrontControllerServletV5() {
        initHandlerMappingMap();
        initHandlerAdapters();
    }

    private void initHandlerMappingMap() {
        handelerMappingMap.put("/front-controller/v5/v3/members/new-form", new MemberFormControllerV3());
        handelerMappingMap.put("/front-controller/v5/v3/members/save", new MemberSaveControllerV3());
        handelerMappingMap.put("/front-controller/v5/v3/members", new MemberListControllerV3());
    }

    private void initHandlerAdapters() {
        handlerAdapters.add(new ControllerV3HandlerAdapter());
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Object handler = getHandler(request);

        if(handler == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        MyHandlerAdapter adapter = getHandlerAdapter(handler);

        ModelView mv = adapter.handle(request, response, handler);
        String viewName = mv.getViewName();
        MyView view = viewResolver(viewName); // 받은 모델 뷰의 뷰 호출을 위해 뷰 리졸버를 호출

        view.render(mv.getModel(), request, response); // 랜더링 -> 뷰가 랜더링되기 위해 모델이 필요함
    }

    private MyHandlerAdapter getHandlerAdapter(Object handler) {
        for (MyHandlerAdapter adapter : handlerAdapters) {
            if (adapter.supports(handler)) {
                return adapter;
            }
        }
        throw new IllegalArgumentException("handler adapter를 찾을 수 없습니다. handler = " + handler); // 디버깅 시 편하도록 어떤 handler가 들어와서 예외가 발생했는지 남기도록
    }

    private Object getHandler(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        return handelerMappingMap.get(requestURI);// 해당 물리 주소가 담긴 모델 뷰 정보를 가져 온다
    }

    private static MyView viewResolver(String viewName) {
        return new MyView("/WEB-INF/views/" + viewName + ".jsp");
    }
}
