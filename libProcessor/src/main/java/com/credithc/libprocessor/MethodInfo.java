package com.credithc.libprocessor;

import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.util.HashMap;

import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;

/**
 * @data: 2019/9/28 0028
 * @author: liyong
 * @desc: class description
 */
public class MethodInfo {

	public static final String SUFFIX = "$PermissionProxy";
	private TypeElement encloseElement;
	private String clsEncloseName;
	public HashMap<Integer, String> grantedMap = new HashMap<>();
	public HashMap<Integer, String> deniedMap = new HashMap<>();
	public HashMap<Integer, String> rationaleMap = new HashMap<>();

	public MethodInfo(TypeElement encloseElement) {
		this.encloseElement = encloseElement;
		clsEncloseName = encloseElement.getSimpleName().toString();
	}

	public void generateCode() {
		MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder("grant")
				.addModifiers(Modifier.PUBLIC)
				.addParameter(TypeName.get(encloseElement.asType()), "activity")
				.addParameter(int.class, "requestCode")
				.addParameter(String[].class, "permissions")
				.returns(void.class);
		for (Integer code : grantedMap.keySet()) {
			methodBuilder.beginControlFlow("switch (requestCode)")
					.addStatement("case $L", code)
					.addStatement("activity.$S(permissions)", grantedMap.get(code))
					.addStatement("break")
					.endControlFlow();
		}

		TypeSpec.Builder typeBuilder = TypeSpec.classBuilder(clsEncloseName + SUFFIX);
	}
}
