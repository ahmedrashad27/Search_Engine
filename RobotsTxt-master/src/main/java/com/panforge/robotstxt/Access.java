/*
 * Copyright 2016 Piotr Andzel.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.panforge.robotstxt;

/**
 * Access.
 */
class Access {
  public static final Access ALLOW = new Access("/", true, true);
  public static final Access DISALLOW = new Access("/", false, true);
  
  private final boolean constant;
  private final String accessPath;
  private final boolean accessAllowed;

  /**
   * Creates instance of the access.
   * @param accessPath access path
   * @param accessAllowed access to the path
   */
  public Access(String accessPath, boolean accessAllowed) {
    this(accessPath, accessAllowed, false);
  }

  /**
   * Creates instance of the access.
   * @param accessPath access path
   * @param accessAllowed access to the path
   */
  Access(String accessPath, boolean accessAllowed, boolean constant) {
    this.constant = constant;
    this.accessPath = accessPath;
    this.accessAllowed = accessAllowed;
  }

  /**
   * Gets path.
   * @return path
   */
  public String getPath() {
    return accessPath;
  }
  
  /**
   * Check if this section gives an access
   * @return 
   */
  public boolean hasAccess() {
    return accessAllowed;
  }
  
  /**
   * Checks if path matches access path
   * @param path path to check
   * @param matchingStrategy matcher
   * @return <code>true</code> if path matches access path
   */
  public boolean matches(String path, MatchingStrategy matchingStrategy) {
    return path!=null && matchingStrategy.matches(accessPath, path);
  }
  
  @Override
  public String toString() {
    return String.format("%s%s: %s", !accessAllowed? "Disallow": "Allow", constant? " (constant)": "",accessPath);
  }
  
}
