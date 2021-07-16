/*
 *    Copyright 2009-2021 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.apache.ibatis.reflection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.domain.misc.RichType;
import org.apache.ibatis.domain.misc.generics.GenericConcrete;
import org.junit.jupiter.api.Test;

class MetaClassTest {

  @Test
  void shouldTestDataTypeOfGenericMethod() {
    ReflectorFactory reflectorFactory = new DefaultReflectorFactory();
    MetaClass meta = MetaClass.forClass(GenericConcrete.class, reflectorFactory);
    assertEquals(Long.class, meta.getGetterType("id"));
    assertEquals(Long.class, meta.getSetterType("id"));
  }

  @Test
  void shouldThrowReflectionExceptionGetGetterType() {
    try {
      ReflectorFactory reflectorFactory = new DefaultReflectorFactory();
      MetaClass meta = MetaClass.forClass(RichType.class, reflectorFactory);
      meta.getGetterType("aString");
      org.junit.jupiter.api.Assertions.fail("should have thrown ReflectionException");
    } catch (ReflectionException expected) {
      assertEquals("There is no getter for property named \'aString\' in \'class org.apache.ibatis.domain.misc.RichType\'", expected.getMessage());
    }
  }

  @Test
  void shouldCheckGetterExistance() {
    ReflectorFactory reflectorFactory = new DefaultReflectorFactory();
    MetaClass meta = MetaClass.forClass(RichType.class, reflectorFactory);
    assertTrue(meta.hasGetter("richField"));
    assertTrue(meta.hasGetter("richProperty"));
    assertTrue(meta.hasGetter("richList"));
    assertTrue(meta.hasGetter("richMap"));
    assertTrue(meta.hasGetter("richList[0]"));

    assertTrue(meta.hasGetter("richType"));
    assertTrue(meta.hasGetter("richType.richField"));
    assertTrue(meta.hasGetter("richType.richProperty"));
    assertTrue(meta.hasGetter("richType.richList"));
    assertTrue(meta.hasGetter("richType.richMap"));
    assertTrue(meta.hasGetter("richType.richList[0]"));

    assertEquals("richType.richProperty", meta.findProperty("richType.richProperty", false));

    assertFalse(meta.hasGetter("[0]"));
  }

  @Test
  void shouldCheckSetterExistance() {
    ReflectorFactory reflectorFactory = new DefaultReflectorFactory();
    MetaClass meta = MetaClass.forClass(RichType.class, reflectorFactory);
    assertTrue(meta.hasSetter("richField"));
    assertTrue(meta.hasSetter("richProperty"));
    assertTrue(meta.hasSetter("richList"));
    assertTrue(meta.hasSetter("richMap"));
    assertTrue(meta.hasSetter("richList[0]"));

    assertTrue(meta.hasSetter("richType"));
    assertTrue(meta.hasSetter("richType.richField"));
    assertTrue(meta.hasSetter("richType.richProperty"));
    assertTrue(meta.hasSetter("richType.richList"));
    assertTrue(meta.hasSetter("richType.richMap"));
    assertTrue(meta.hasSetter("richType.richList[0]"));

    assertFalse(meta.hasSetter("[0]"));
  }

  @Test
  void shouldCheckTypeForEachGetter() {
    ReflectorFactory reflectorFactory = new DefaultReflectorFactory();
    MetaClass meta = MetaClass.forClass(RichType.class, reflectorFactory);
    assertEquals(String.class, meta.getGetterType("richField"));
    assertEquals(String.class, meta.getGetterType("richProperty"));
    assertEquals(List.class, meta.getGetterType("richList"));
    assertEquals(Map.class, meta.getGetterType("richMap"));
    assertEquals(List.class, meta.getGetterType("richList[0]"));

    assertEquals(RichType.class, meta.getGetterType("richType"));
    assertEquals(String.class, meta.getGetterType("richType.richField"));
    assertEquals(String.class, meta.getGetterType("richType.richProperty"));
    assertEquals(List.class, meta.getGetterType("richType.richList"));
    assertEquals(Map.class, meta.getGetterType("richType.richMap"));
    assertEquals(List.class, meta.getGetterType("richType.richList[0]"));
  }

  @Test
  void shouldCheckTypeForEachSetter() {
    ReflectorFactory reflectorFactory = new DefaultReflectorFactory();
    MetaClass meta = MetaClass.forClass(RichType.class, reflectorFactory);
    assertEquals(String.class, meta.getSetterType("richField"));
    assertEquals(String.class, meta.getSetterType("richProperty"));
    assertEquals(List.class, meta.getSetterType("richList"));
    assertEquals(Map.class, meta.getSetterType("richMap"));
    assertEquals(List.class, meta.getSetterType("richList[0]"));

    assertEquals(RichType.class, meta.getSetterType("richType"));
    assertEquals(String.class, meta.getSetterType("richType.richField"));
    assertEquals(String.class, meta.getSetterType("richType.richProperty"));
    assertEquals(List.class, meta.getSetterType("richType.richList"));
    assertEquals(Map.class, meta.getSetterType("richType.richMap"));
    assertEquals(List.class, meta.getSetterType("richType.richList[0]"));
  }

  @Test
  void shouldCheckGetterAndSetterNames() {
    ReflectorFactory reflectorFactory = new DefaultReflectorFactory();
    MetaClass meta = MetaClass.forClass(RichType.class, reflectorFactory);
    assertEquals(5, meta.getGetterNames().length);
    assertEquals(5, meta.getSetterNames().length);
  }

  @Test
  void shouldFindPropertyName() {
    ReflectorFactory reflectorFactory = new DefaultReflectorFactory();
    MetaClass meta = MetaClass.forClass(RichType.class, reflectorFactory);
    assertEquals("richField", meta.findProperty("RICHfield"));
  }
  private class Author {
    private Integer id;
    private String name;
    private Integer age;
    /** 一个作者对应多篇文章 */
    private Article[] articles;

    // 省略 getter/setter
  }

  private class Article {
    private Integer id;
    private String title;
    private String content;
    /** 一篇文章对应一个作者 */
    private Author author;

    // 省略 getter/setter
  }

  @Test
  public void testHasSetter() {
    // 为 Author 创建元信息对象
    MetaClass authorMeta = MetaClass.forClass(Author.class, new DefaultReflectorFactory());
    System.out.println("------------☆ Author ☆------------");
    System.out.println("id -> " + authorMeta.hasSetter("id[]"));
    System.out.println("name -> " + authorMeta.hasSetter("name"));
    System.out.println("age -> " + authorMeta.hasSetter("age"));
    // 检测 Author 中是否包含 Article[] 的 setter
    System.out.println("articles -> " + authorMeta.hasSetter("articles"));
//    这说明 PropertyTokenizer 对数组和复合属性均进行了处理。那它是如何处理的呢
    System.out.println("articles[] -> " + authorMeta.hasSetter("articles[]"));
    System.out.println("title -> " + authorMeta.hasSetter("title"));

    // 为 Article 创建元信息对象
    MetaClass articleMeta = MetaClass.forClass(Article.class, new DefaultReflectorFactory());
    System.out.println("\n------------☆ Article ☆------------");
    System.out.println("id -> " + articleMeta.hasSetter("id"));
    System.out.println("title -> " + articleMeta.hasSetter("title"));
    System.out.println("content -> " + articleMeta.hasSetter("content"));
    // 下面两个均为复杂属性，分别检测 Article 类中的 Author 类是否包含 id 和 name 的 setter 方法
    System.out.println("author.id -> " + articleMeta.hasSetter("author.id"));
    System.out.println("author.name -> " + articleMeta.hasSetter("author.name"));
  }

}
