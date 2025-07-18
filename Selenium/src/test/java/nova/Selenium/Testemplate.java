package nova.Selenium;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import org.testng.IAnnotationTransformer;
import org.testng.IExecutionListener;
import org.testng.ITestClass;
import org.testng.annotations.ITestAnnotation;

public class Testemplate implements IAnnotationTransformer, IExecutionListener {

	public void transform(Class testclass, ITestAnnotation annotation, Method m, Constructor cons) {

		if (testclass.getName().equals("draganddrop")) {

			annotation.setInvocationCount(3);
		}
		if (annotation.getDescription().equals("draggable")) {

			annotation.setEnabled(false);
		}

	}

	public void OnExecutionStart() {

		Long starttime = System.currentTimeMillis();
		System.out.println(starttime);
	}

}
