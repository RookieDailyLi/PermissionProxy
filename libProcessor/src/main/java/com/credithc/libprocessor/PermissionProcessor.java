package com.credithc.libprocessor;

import com.credithc.libannotation.PermissionDenied;
import com.credithc.libannotation.PermissionGranted;
import com.credithc.libannotation.PermissionRationale;
import com.google.auto.service.AutoService;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

/**
 * @author liyong
 * @date 2019/9/27 15:57
 * @description
 */
@AutoService(Processor.class)
public class PermissionProcessor extends AbstractProcessor {
	private Elements elementUtil;
	private Types typeUtil;
	private Filer filer;
	private Messager messager;
	private HashMap<String, MethodInfo> clsMethodMap = new HashMap<>();

	@Override

	public synchronized void init(ProcessingEnvironment processingEnv) {
		elementUtil = processingEnv.getElementUtils();
		typeUtil = processingEnv.getTypeUtils();
		filer = processingEnv.getFiler();
		messager = processingEnv.getMessager();
	}

	@Override
	public Set<String> getSupportedAnnotationTypes() {
		HashSet<String> supAnnotationTypes = new LinkedHashSet<>();
		supAnnotationTypes.add(PermissionGranted.class.getCanonicalName());
		supAnnotationTypes.add(PermissionDenied.class.getCanonicalName());
		supAnnotationTypes.add(PermissionRationale.class.getCanonicalName());
		return supAnnotationTypes;
	}

	@Override
	public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
		if (annotations == null || annotations.size() == 0) {
			return true;
		}

		handleAnnotationInfo(roundEnv, PermissionGranted.class);
		handleAnnotationInfo(roundEnv, PermissionDenied.class);
		handleAnnotationInfo(roundEnv, PermissionRationale.class);

		for (String clsName : clsMethodMap.keySet()) {
			MethodInfo methodInfo = clsMethodMap.get(clsName);
			methodInfo.generateCode();
		}
		return true;
	}

	private void handleAnnotationInfo(RoundEnvironment roundEnv, Class permissionClass) {
		Set<? extends Element> elementSet = roundEnv.getElementsAnnotatedWith(permissionClass);
		for (Element element : elementSet) {
			if (checkMethodValid(element)) {
				ExecutableElement executableElement = (ExecutableElement) element;
				TypeElement clsElement = (TypeElement) executableElement.getEnclosingElement();
				String clsName = clsElement.getQualifiedName().toString();
				MethodInfo methodInfo = clsMethodMap.get(clsName);
				if (methodInfo == null) {
					methodInfo = new MethodInfo(elementUtil, filer, messager, clsElement);
					clsMethodMap.put(clsName, methodInfo);
				}
				Annotation annotation = executableElement.getAnnotation(permissionClass);
				String methodName = executableElement.getSimpleName().toString();

				List<? extends VariableElement> parameterElement = executableElement.getParameters();
				if (parameterElement == null || parameterElement.size() == 0) {
					String msg = "method %s marked by %s must hava a parameter String[]";
					throw new IllegalArgumentException(String.format(msg, methodName, annotation.getClass().getSimpleName()));
				}
				if (annotation instanceof PermissionGranted) {
					int requestCode = ((PermissionGranted) annotation).value();
					methodInfo.grantedMap.put(requestCode, methodName);
				} else if (annotation instanceof PermissionDenied) {
					int requestCode = ((PermissionDenied) annotation).value();
					methodInfo.deniedMap.put(requestCode, methodName);
				} else if (annotation instanceof PermissionRationale) {
					int requestCode = ((PermissionRationale) annotation).value();
					methodInfo.rationaleMap.put(requestCode, methodName);
				}
			}
		}
	}

	private boolean checkMethodValid(Element element) {
		if (element.getKind() != ElementKind.METHOD) {
			return false;
		}

		if (element.getModifiers().contains(Modifier.PRIVATE)) {
			return false;
		}

		if (element.getModifiers().contains(Modifier.ABSTRACT)) {
			return false;
		}
		return true;
	}

	@Override
	public SourceVersion getSupportedSourceVersion() {
		return SourceVersion.latest();
	}
}
