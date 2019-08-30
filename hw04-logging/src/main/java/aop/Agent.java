package aop;

import annotation.Log;
import javassist.*;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;

public class Agent {
    public static void premain(String agentArgs, Instrumentation inst) {
        inst.addTransformer(new ClassFileTransformer() {
            @Override
            public byte[] transform(ClassLoader loader, String className,
                                    Class<?> classBeingRedefined,
                                    ProtectionDomain protectionDomain,
                                    byte[] classfileBuffer) {
                try {
                    CtClass cc = getCompileTimeClass(className);
                    if (cc.getName().equals("annotated.TestLoggingImpl")) {
                        byte[] bytes = addProxyMethods(cc, classfileBuffer);
                        return bytes;
                    }
                } catch (Exception e) {
                    System.out.println("Error: " + e.getMessage());
                    return classfileBuffer;
                }
                return classfileBuffer;
            }
        });
    }

    private static CtClass getCompileTimeClass(String className) throws NotFoundException {
        ClassPool classPool = ClassPool.getDefault();
        String classNamePoint = className.replaceAll("/", ".");
        return classPool.get(classNamePoint);
    }

    private static byte[] addProxyMethods(CtClass clazz, byte[] originalClass) throws IOException, CannotCompileException, NotFoundException {
        CtMethod[] methods = clazz.getDeclaredMethods();
        byte[] result = originalClass;
        for (CtMethod method : methods) {
            if (method.hasAnnotation(Log.class)) {
                result = addProxyMethod(clazz, method, result);
            }
        }

        return result;
    }

    private static byte[] addProxyMethod(CtClass ctClass, CtMethod method, byte[] classBuffer) throws IOException, CannotCompileException, NotFoundException {
        CtClass[] parametersTypes = new CtClass[]{};
        try {
            parametersTypes = method.getParameterTypes();
        } catch (NotFoundException ignored) {
        }
        int parametersCount = parametersTypes.length;
        String codeToInsert = "System.out.print(\"executed method: " + method.getName() + ". \");";

        if (parametersCount > 0) {
            for (int i = 1; i <= parametersCount; i++) {
                codeToInsert += "System.out.print(\" param" + i + ":\"); ";
                if (i == parametersCount) {
                    codeToInsert += "System.out.println($" + i + ");";
                } else {
                    codeToInsert += "System.out.print($" + i + "); ";
                }
            }
        }

        try {
            method.insertBefore("{ "+ codeToInsert +" }");
        } catch (CannotCompileException e) {
            e.printStackTrace();
        }

        ctClass.writeFile();
        byte[] finalClass = ctClass.toBytecode();

        try (OutputStream fos = new FileOutputStream("proxy.class")) {
            fos.write(finalClass);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return finalClass;
    }
}
