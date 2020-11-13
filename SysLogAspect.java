package com.example.sysLog;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import org.aspectj.lang.ProceedingJoinPoint;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

/**
 * @ClassName : SysLogAspect
 * @Description :  系统日志切面
 * @Author : 郭兵
 * @Date: 2020-09-28 11:57
 */
@Aspect // 使用@Aspect注解声明一个切面
@Component
public class SysLogAspect {
    private final static Logger logger     = LoggerFactory.getLogger(SysLogAspect.class);
    /** 换行符 */
    private static final String LINE_SEPARATOR = System.lineSeparator();


    @Autowired
   // private SysLogService sysLogService;

    /**
     * 这里我们使用注解的形式 当然，我们也可以通过切点表达式直接指定需要拦截的package,需要拦截的class 以及 method 切点表达式:
     * execution(...)
     */
    @Pointcut("@annotation(com.example.sysLog.SysLog)")
    public void logPointCut(){}
    /**3
     * 在切点之前织入
     * @param joinPoint
     * @throws Throwable
     */
    @Before("logPointCut()")
    public void doBefore(JoinPoint joinPoint) throws Throwable {
        // 开始打印请求日志
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();

        // 获取 @WebLog 注解的描述信息
        String methodDescription = getAspectLogDescription(joinPoint);

        // 打印请求相关参数
        logger.info("========================================== Start ==========================================");
        // 打印请求 url
        logger.info("URL            : {}", request.getRequestURL().toString());
        // 打印描述信息
        logger.info("Description    : {}", methodDescription);
        // 打印 Http method
        logger.info("HTTP Method    : {}", request.getMethod());
        // 打印调用 controller 的全路径以及执行方法
        logger.info("Class Method   : {}.{}", joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName());
        // 打印请求的 IP
        logger.info("IP             : {}", request.getRemoteAddr());
        // 打印请求入参
        logger.info("Request Args   : {}", JSONObject.toJSON(joinPoint.getArgs()));
    }
    /**4
     * 在切点之后织入
     * @throws Throwable
     */
    @After("logPointCut()")
    public void doAfter(){
        logger.info("=========================================== End ===========================================" + LINE_SEPARATOR);
    }
    /**
      * @Author 郭兵
      * @Description : 在切点之后织入   与上面只取一个
      * @Date  2020/9/28 14:48
     **/
//    @AfterReturning(returning = "result", pointcut = "logPointCut()")
//    public void doAfter(Object result){
//        // 接口结束后换行，方便分割查看
//        // 打印出参
//        logger.info("Response Args  : {}", JSONObject.toJSON(result));
//        logger.info("=========================================== End ===========================================" + LINE_SEPARATOR);
//    }
    /**
     * 环绕2
     * @param proceedingJoinPoint
     * @return
     * @throws Throwable
     */
//    @Around("logPointCut()")
//    public Object doAround(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
//        long startTime = System.currentTimeMillis();
//        //进入doBefore
//        Object result = proceedingJoinPoint.proceed();
//        // 打印出参
//        logger.info("Response Args  : {}", JSONObject.toJSON(result));
//        // 执行耗时
//        logger.info("Time-Consuming : {} ms", System.currentTimeMillis() - startTime);
//        return result;
//    }

    /**
     * 环绕通知 @Around ， 当然也可以使用 @Before (前置通知) @After (后置通知)
     *1
     * @param point
     * @return
     * @throws Throwable
     */
//    @Around("logPointCut()")
//    public Object around(ProceedingJoinPoint point) throws Throwable {
//        long beginTime = System.currentTimeMillis();
//        //进入doAround方法
//        Object result = point.proceed();
//        long time = System.currentTimeMillis() - beginTime;
//        try {
//            saveLog(point, time);
//        } catch (Exception e) {
//        }
//        return result;
//    }
    /**
     * 获取切面注解的描述
     *
     * @param joinPoint 切点
     * @return 描述信息
     * @throws Exception
     */
    public String getAspectLogDescription(JoinPoint joinPoint)
            throws Exception {
        // 获取执行方法的类的名称（包名加类名）
        String targetName = joinPoint.getTarget().getClass().getName();
        String methodName = joinPoint.getSignature().getName();
        Object[] arguments = joinPoint.getArgs();
        Class targetClass = Class.forName(targetName);
        Method[] methods = targetClass.getMethods();
        StringBuilder description = new StringBuilder("");
        for (Method method : methods) {
            if (method.getName().equals(methodName)) {
                Class[] clazzs = method.getParameterTypes();
                if (clazzs.length == arguments.length) {
                    description.append(method.getAnnotation(SysLog.class).description());
                    break;
                }
            }
        }
        return description.toString();
    }


    /**
     * 保存日志
     *
     * @param joinPoint
     * @param time
     */
    private void saveLog(ProceedingJoinPoint joinPoint, long time) {
//        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
//        Method method = signature.getMethod();
//        SysLogBO sysLogBO = new SysLogBO();
//
//        sysLogBO.setExeuTime(time);
//        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
//        sysLogBO.setCreateDate(dateFormat.format(new Date()));
//        SysLog sysLog = method.getAnnotation(SysLog.class);
//        if (sysLog != null) {
//            // 注解上的描述
//            sysLogBO.setRemark(sysLog.value());
//        }
//        // 请求的 类名、方法名
//        String className = joinPoint.getTarget().getClass().getName();
//        String methodName = signature.getName();
//        sysLogBO.setClassName(className);
//        sysLogBO.setMethodName(methodName);
//        // 请求的参数
//        Object[] args = joinPoint.getArgs();
//        try {
//            List<String> list = new ArrayList<String>();
//            for (Object o : args) {
//                list.add(new Gson().toJson(o));
//            }
//            sysLogBO.setParams(list.toString());
//        } catch (Exception e) {
//        }
//        sysLogService.save(sysLogBO);
    }
}