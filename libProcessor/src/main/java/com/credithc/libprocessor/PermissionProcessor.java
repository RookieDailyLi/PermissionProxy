package com.credithc.libprocessor;

import com.credithc.libannotation.PermissionDenied;
import com.credithc.libannotation.PermissionGranted;
import com.credithc.libannotation.PermissionRationale;
import com.google.auto.service.AutoService;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
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

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        elementUtil = processingEnv.getElementUtils();
        typeUtil = processingEnv.getTypeUtils();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        HashSet<String> supAnnotationTypes = new LinkedHashSet<>();
        supAnnotationTypes.add(PermissionGranted.class.getCanonicalName());
        supAnnotationTypes.add(PermissionDenied.class.getCanonicalName());
        supAnnotationTypes.add(PermissionRationale.class.getCanonicalName());
        return super.getSupportedAnnotationTypes();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {

        return true;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latest();
    }
}
