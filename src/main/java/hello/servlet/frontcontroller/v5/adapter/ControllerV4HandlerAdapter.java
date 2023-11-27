package hello.servlet.frontcontroller.v5.adapter;

import hello.servlet.frontcontroller.ModelView;
import hello.servlet.frontcontroller.MyView;
import hello.servlet.frontcontroller.v4.ControllerV4;
import hello.servlet.frontcontroller.v5.MyHandlerAdapter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ControllerV4HandlerAdapter implements MyHandlerAdapter {
    @Override
    public boolean supports(Object handler) {
        return (handler instanceof ControllerV4);
    }

    @Override
    public ModelView handle(HttpServletRequest request, HttpServletResponse response, Object handler) throws ServletException, IOException {
        ControllerV4 controller = (ControllerV4) handler;

        Map<String, String> paramMap = createParamMap(request);
        Map<String, Object> model = new HashMap<>();
        String viewName = controller.process(paramMap, model); // viewName을 반환한다. -> ModelView를 반환하지 못했으면 ModelView를 생성해서라도 넣어주어야 한다.

        // 모델 뷰라 뷰를 초기화하고 모델도 초기화 해주어야 한다.
        ModelView mv = new ModelView(viewName);
        mv.setModel(model); // 그래서 model을 넣어준다.

        return mv;
    }

    private static Map<String, String> createParamMap(HttpServletRequest request) {
        Map<String, String> paramMap = new HashMap<>();
        request.getParameterNames().asIterator()
                .forEachRemaining(paramName -> paramMap.put(paramName, request.getParameter(paramName)));
        return paramMap;
    }
}
