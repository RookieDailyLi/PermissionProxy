package com.credithc.libprocessor;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.HashMap;

import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;

/**
 * @data: 2019/9/28 0028
 * @author: liyong
 * @desc: class description
 */
public class MethodInfo {

	public static final String SUFFIX = "$PermissionProxy";
	private Elements elementUtil;
	private Filer filer;
	private Messager messager;
	private TypeElement encloseElement;
	private String clsSimpleName;
	private String packageName;
	public HashMap<Integer, String> grantedMap = new HashMap<>();
	public HashMap<Integer, String> deniedMap = new HashMap<>();
	public HashMap<Integer, String> rationaleMap = new HashMap<>();

	public MethodInfo(Elements elementUtil, Filer filer, Messager messager, TypeElement encloseElement) {
		this.elementUtil = elementUtil;
		this.filer = filer;
		this.messager = messager;
		this.encloseElement = encloseElement;
		clsSimpleName = encloseElement.getSimpleName().toString();
		packageName = elementUtil.getPackageOf(encloseElement).getQualifiedName().toString();
	}

	public void generateCode() {
		MethodSpec.Builder methodBuilderGrant = MethodSpec.methodBuilder("grant")
				.addModifiers(Modifier.PUBLIC)
				.addAnnotation(Override.class)
				.addParameter(TypeName.get(encloseElement.asType()), "activity")
				.addParameter(String[].class, "permissions")
				.addParameter(int.class, "requestCode")
				.returns(void.class);
		for (Integer code : grantedMap.keySet()) {
			methodBuilderGrant.beginControlFlow("switch (requestCode)")
					.addCode("case $L:\n", code)
					.addStatement("activity." + grantedMap.get(code) + "(permissions)")
					.addStatement("break")
					.endControlFlow();
		}

		MethodSpec.Builder methodBuilderDeny = MethodSpec.methodBuilder("deny")
				.addModifiers(Modifier.PUBLIC)
				.addAnnotation(Override.class)
				.addParameter(TypeName.get(encloseElement.asType()), "activity")
				.addParameter(String[].class, "permissions")
				.addParameter(int.class, "requestCode")
				.returns(void.class);
		for (Integer code : deniedMap.keySet()) {
			methodBuilderDeny.beginControlFlow("switch (requestCode)")
					.addCode("case $L:\n", code)
					.addStatement("activity." + deniedMap.get(code) + "(permissions)")
					.addStatement("break")
					.endControlFlow();
		}

		MethodSpec.Builder methodBuilderRationale = MethodSpec.methodBuilder("rationale")
				.addModifiers(Modifier.PUBLIC)
				.addAnnotation(Override.class)
				.addParameter(TypeName.get(encloseElement.asType()), "activity")
				.addParameter(String[].class, "permissions")
				.addParameter(int.class, "requestCode")
				.addParameter(TypeName.get(elementUtil.getTypeElement("com.credithc.libpermissionhelper.RationaleCallBack").asType()), "callBack")
				.returns(void.class);
		for (Integer code : rationaleMap.keySet()) {
			methodBuilderRationale.beginControlFlow("switch (requestCode)")
					.addCode("case $L:\n", code)
					.addStatement("activity." + rationaleMap.get(code) + "(permissions,callBack)")
					.addStatement("break")
					.endControlFlow();
		}


		TypeSpec typeSpec = TypeSpec.classBuilder(clsSimpleName + SUFFIX)
				.addSuperinterface(ParameterizedTypeName.get(
						ClassName.get("com.credithc.libpermissionhelper", "PermissionProxy")
						, ClassName.get(encloseElement)))
				.addModifiers(Modifier.PUBLIC)
				.addMethod(methodBuilderGrant.build())
				.addMethod(methodBuilderDeny.build())
				.addMethod(methodBuilderRationale.build())
				.build();

		try {
			JavaFile.builder(packageName, typeSpec)
					.build()
					.writeTo(filer);
		} catch (IOException e) {
			e.printStackTrace();
			messager.printMessage(Diagnostic.Kind.NOTE, e.toString(), encloseElement);
		}
	}
}
