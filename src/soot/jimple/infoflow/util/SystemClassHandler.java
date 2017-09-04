package soot.jimple.infoflow.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import soot.RefType;
import soot.SootField;
import soot.SootMethod;
import soot.Type;
import soot.jimple.infoflow.data.AccessPath;

/**
 * Utility class for checking whether methods belong to system classes
 * 
 * @author Steven Arzt
 */
public class SystemClassHandler {

	/**
	 * Checks whether the given class name belongs to a system package
	 * 
	 * @param className
	 *            The class name to check
	 * @return True if the given class name belongs to a system package, otherwise
	 *         false
	 */
	public static boolean isClassInSystemPackage(String className) {
		boolean b = false;
		if (className.startsWith("android.") || className.startsWith("java.") || className.startsWith("javax.")
				|| className.startsWith("sun.") || className.startsWith("com.google.")
				|| className.startsWith("org.omg.") || className.startsWith("org.w3c.dom.")) {
			/*
			 * try { BufferedWriter out = new BufferedWriter(new
			 * FileWriter("/home/ehsan/Desktop/systemClasses.out",true));
			 * out.write(className+"\r\n"); //Replace with the string //you are trying to
			 * write out.close(); } catch (IOException e) {
			 * System.err.println("ERROR:: writting sysClasses in file");
			 * 
			 * }
			 */
			int SDK_Version = 25;
			String fileName = "/" + className.substring(className.lastIndexOf('.') + 1) + ".java";
			String targetPath = "/media/ehsan/a2c41319-a56d-4856-b979-aeaebaea4e50/Programs/Mock-and-Stubs/";
			String SDKPath = "/media/ehsan/a2c41319-a56d-4856-b979-aeaebaea4e50/Programs/Android/android-sdk-linux/sources/android-";

			String relativePath = className.replace(className.substring(className.lastIndexOf('.')), "").replace('.',
					'/');
			copyFromSDK(fileName, targetPath, SDKPath, relativePath, SDK_Version);
			b = true;
		}
		return b;
	}

	private static void copyFromSDK(String fileName, String targetPath, String SDKPath, String relativePath,
			int SDK_Version) {
		SDKPath += SDK_Version + "/";
		if (!Files.exists(Paths.get(targetPath + relativePath + fileName), LinkOption.NOFOLLOW_LINKS)) {

			new File(targetPath + relativePath).mkdirs();
			try {
				Files.copy(Paths.get(SDKPath + relativePath + fileName),
						Paths.get(targetPath + relativePath + fileName), StandardCopyOption.REPLACE_EXISTING);
			} catch (IOException e) {
				System.err.println("ERROR:: \"" + SDKPath + relativePath + fileName + "\" NOT FOUND!!!!!");
				if (SDK_Version != 0) {
					SDK_Version--;
					copyFromSDK(fileName, targetPath, SDKPath, relativePath, SDK_Version);
				}
			}
		}
	}

	/**
	 * Checks whether the type belongs to a system package
	 * 
	 * @param type
	 *            The type to check
	 * @return True if the given type belongs to a system package, otherwise false
	 */
	public static boolean isClassInSystemPackage(Type type) {
		if (type instanceof RefType)
			return isClassInSystemPackage(((RefType) type).getSootClass().getName());
		return false;
	}

	/**
	 * If the base object is tainted and in a system class, but we reference some
	 * field in an application object derived from the system class, no tainting
	 * happens. The system class cannot access or know about user-code fields. This
	 * leaves reflection aside, but we don't support reflection anyway.
	 * 
	 * @param taintedPath
	 *            The access path of the incoming taint
	 * @param method
	 *            The method that gets called
	 * @return True if the given taint is visible to the callee, otherwise false
	 */
	public static boolean isTaintVisible(AccessPath taintedPath, SootMethod method) {
		// If we don't know anything about the tainted access path, we have to
		// conservatively assume that it's visible in the calllee
		if (taintedPath == null)
			return true;

		// If the complete base object is tainted, this is always visible
		if (!taintedPath.isInstanceFieldRef())
			return true;

		// User code can cast objects to arbitrary user and system types
		if (!SystemClassHandler.isClassInSystemPackage(method.getDeclaringClass().getName()))
			return true;

		// Check whether we have a user-defined field
		for (SootField fld : taintedPath.getFields()) {
			if (!SystemClassHandler.isClassInSystemPackage(fld.getType()))
				return false;
			if (!SystemClassHandler.isClassInSystemPackage(fld.getDeclaringClass().getType()))
				return false;
		}

		// We don't have a reason to believe that this taint is invisible to the
		// callee
		return true;
	}

}
