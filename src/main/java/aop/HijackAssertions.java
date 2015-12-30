package aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class HijackAssertions {

  @Pointcut("call(* *.assertEquals(..))")
  public void defineEntryPoint() {
  }

  @Around("defineEntryPoint()")
  public void aaa(ProceedingJoinPoint joinPoint) throws Throwable {
    try {
      joinPoint.proceed();
    } catch (AssertionError ex) {
      // DO NOT RETHROW THE EXCEPTION
      System.out.println("Hijacked!");
    }
  }

}
