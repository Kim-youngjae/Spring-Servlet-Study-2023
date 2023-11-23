package hello.servlet.frontcontroller.v3;

import hello.servlet.frontcontroller.ModelView;

import java.util.Map;

public interface ControllerV3 {
    ModelView process(Map<String, String> paramMap);
}
