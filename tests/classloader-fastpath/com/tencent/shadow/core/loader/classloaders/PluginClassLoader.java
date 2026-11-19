package com.tencent.shadow.core.loader.classloaders;
public class PluginClassLoader extends ClassLoader{
 private final ClassLoader specialClassLoader;public int probes,defaultProbes,finds;private byte[] data;
 public PluginClassLoader(boolean standalone,byte[] bytes){super(PluginClassLoader.class.getClassLoader());specialClassLoader=standalone?new ClassLoader(null){public Class<?> loadClass(String n)throws ClassNotFoundException{probes++;if(n.indexOf('.')<0)defaultProbes++;return super.loadClass(n);}}:null;data=bytes;}
 protected Class<?> findClass(String n)throws ClassNotFoundException{finds++;if(n.equals("ObfuscatedFixture"))return defineClass(n,data,0,data.length);throw new ClassNotFoundException(n);}
 protected Class<?> loadClass(String n,boolean r)throws ClassNotFoundException{Class<?> c=findLoadedClass(n);if(c!=null)return c;if(specialClassLoader==null)return super.loadClass(n,r);try{return specialClassLoader.loadClass(n);}catch(ClassNotFoundException expected){}return findClass(n);}
}
