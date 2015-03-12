package org.lunifera.dsl.ext.sample.cpp.qt;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import org.lunifera.dsl.dto.lib.MappingContext;
import org.lunifera.dsl.ext.cpp.qt.lib.types.QString;
import org.lunifera.dsl.ext.cpp.qt.lib.types.annotation.SimpleName;
import org.lunifera.dsl.ext.sample.cpp.qt.MyOther;
import org.lunifera.runtime.common.annotations.Dispose;
import org.lunifera.runtime.common.annotations.DomainReference;

@SuppressWarnings("all")
public class MyEntity implements Serializable, PropertyChangeListener {
  private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);
  
  @Dispose
  private boolean disposed;
  
  @SimpleName("nme")
  private QString name;
  
  private int age;
  
  @DomainReference
  private List<MyOther> others;
  
  /**
   * Returns true, if the object is disposed. 
   * Disposed means, that it is prepared for garbage collection and may not be used anymore. 
   * Accessing objects that are already disposed will cause runtime exceptions.
   */
  public boolean isDisposed() {
    return this.disposed;
  }
  
  /**
   * @see PropertyChangeSupport#addPropertyChangeListener(PropertyChangeListener)
   */
  public void addPropertyChangeListener(final PropertyChangeListener listener) {
    propertyChangeSupport.addPropertyChangeListener(listener);
  }
  
  /**
   * @see PropertyChangeSupport#addPropertyChangeListener(String, PropertyChangeListener)
   */
  public void addPropertyChangeListener(final String propertyName, final PropertyChangeListener listener) {
    propertyChangeSupport.addPropertyChangeListener(propertyName, listener);
  }
  
  /**
   * @see PropertyChangeSupport#removePropertyChangeListener(PropertyChangeListener)
   */
  public void removePropertyChangeListener(final PropertyChangeListener listener) {
    propertyChangeSupport.removePropertyChangeListener(listener);
  }
  
  /**
   * @see PropertyChangeSupport#removePropertyChangeListener(String, PropertyChangeListener)
   */
  public void removePropertyChangeListener(final String propertyName, final PropertyChangeListener listener) {
    propertyChangeSupport.removePropertyChangeListener(propertyName, listener);
  }
  
  /**
   * @see PropertyChangeSupport#firePropertyChange(String, Object, Object)
   */
  public void firePropertyChange(final String propertyName, final Object oldValue, final Object newValue) {
    propertyChangeSupport.firePropertyChange(propertyName, oldValue, newValue);
  }
  
  /**
   * Checks whether the object is disposed.
   * @throws RuntimeException if the object is disposed.
   */
  private void checkDisposed() {
    if (isDisposed()) {
      throw new RuntimeException("Object already disposed: " + this);
    }
  }
  
  /**
   * Calling dispose will destroy that instance. The internal state will be 
   * set to 'disposed' and methods of that object must not be used anymore. 
   * Each call will result in runtime exceptions.<br/>
   * If this object keeps composition containments, these will be disposed too. 
   * So the whole composition containment tree will be disposed on calling this method.
   */
  @Dispose
  public void dispose() {
    if (isDisposed()) {
      return;
    }
    firePropertyChange("disposed", this.disposed, this.disposed = true);
  }
  
  /**
   * Returns the name property or <code>null</code> if not present.
   */
  public QString getName() {
    return this.name;
  }
  
  /**
   * Sets the <code>name</code> property to this instance.
   * 
   * @param name - the property
   * @throws RuntimeException if instance is <code>disposed</code>
   * 
   */
  public void setName(final QString name) {
    firePropertyChange("name", this.name, this.name = name );
  }
  
  /**
   * Returns the age property or <code>null</code> if not present.
   */
  public int getAge() {
    return this.age;
  }
  
  /**
   * Sets the <code>age</code> property to this instance.
   * 
   * @param age - the property
   * @throws RuntimeException if instance is <code>disposed</code>
   * 
   */
  public void setAge(final int age) {
    firePropertyChange("age", this.age, this.age = age );
  }
  
  /**
   * Returns an unmodifiable list of others.
   */
  public List<MyOther> getOthers() {
    return Collections.unmodifiableList(internalGetOthers());
  }
  
  /**
   * Returns the list of <code>MyOther</code>s thereby lazy initializing it. For internal use only!
   * 
   * @return list - the resulting list
   * 
   */
  private List<MyOther> internalGetOthers() {
    if (this.others == null) {
      this.others = new java.util.ArrayList<MyOther>();
    }
    return this.others;
  }
  
  /**
   * Adds the given myOther to this object. <p>
   * Since the reference is a composition reference, the opposite reference <code>MyOther#entity</code> of the <code>myOther</code> will be handled automatically and no further coding is required to keep them in sync.<p>
   * See {@link MyOther#setEntity(MyOther)}.
   * 
   * @param myOther - the property
   * @throws RuntimeException if instance is <code>disposed</code>
   * 
   */
  public void addToOthers(final MyOther myOther) {
    checkDisposed();
    
    myOther.setEntity(this);
  }
  
  /**
   * Removes the given myOther from this object. <p>
   * 
   * @param myOther - the property
   * @throws RuntimeException if instance is <code>disposed</code>
   * 
   */
  public void removeFromOthers(final MyOther myOther) {
    checkDisposed();
    
    myOther.setEntity(null);
  }
  
  /**
   * For internal use only!
   */
  void internalAddToOthers(final MyOther myOther) {
    internalGetOthers().add(myOther);
  }
  
  /**
   * For internal use only!
   */
  void internalRemoveFromOthers(final MyOther myOther) {
    internalGetOthers().remove(myOther);
  }
  
  /**
   * Sets the <code>others</code> property to this instance.
   * Since the reference has an opposite reference, the opposite <code>MyOther#
   * entity</code> of the <code>others</code> will be handled automatically and no 
   * further coding is required to keep them in sync.<p>
   * See {@link MyOther#setEntity(MyOther)
   * 
   * @param others - the property
   * @throws RuntimeException if instance is <code>disposed</code>
   * 
   */
  public void setOthers(final List<MyOther> others) {
    checkDisposed();
    for (MyOther dto : internalGetOthers().toArray(new MyOther[this.others.size()])) {
    	removeFromOthers(dto);
    }
    
    if(others == null) {
    	return;
    }
    
    for (MyOther dto : others) {
    	addToOthers(dto);
    }
  }
  
  public MyEntity createDto() {
    return new MyEntity();
  }
  
  public MyEntity copy(final MappingContext context) {
    checkDisposed();
    
    if (context == null) {
    	throw new IllegalArgumentException("Context must not be null!");
    }
    
    if(context.isMaxLevel()){
    	return null;
    }
    
    // if context contains a copied instance of this object
    // then return it
    MyEntity newDto = context.get(this);
    if(newDto != null){
    	return newDto;
    }
    
    try{
    	context.increaseLevel();
    	
    	newDto = createDto();
    	context.register(this, newDto);
    	
    	// first copy the containments and attributes
    	copyContainments(this, newDto, context);
    	
    	// then copy cross references to ensure proper
    	// opposite references are copied too.
    	copyCrossReferences(this, newDto, context);
    } finally {
    	context.decreaseLevel();
    }
    
    return newDto;
  }
  
  public void copyContainments(final MyEntity dto, final MyEntity newDto, final MappingContext context) {
    checkDisposed();
    
    if (context == null) {
    	throw new IllegalArgumentException("Context must not be null!");
    }
    
    
    // copy attributes and beans (beans if derived from entity model)
    // copy name
    newDto.setName(getName());
    // copy age
    newDto.setAge(getAge());
    
    // copy containment references (cascading is true)
  }
  
  public void copyCrossReferences(final MyEntity dto, final MyEntity newDto, final org.lunifera.dsl.dto.lib.MappingContext context) {
    checkDisposed();
    
    if (context == null) {
    	throw new IllegalArgumentException("Context must not be null!");
    }
    
    
    // copy cross references (cascading is false)
    // copy list of others dtos
    for(org.lunifera.dsl.ext.sample.cpp.qt.MyOther _dto : getOthers()) {
    	newDto.addToOthers(_dto.copy(context));
    }
  }
  
  public void propertyChange(final java.beans.PropertyChangeEvent event) {
    Object source = event.getSource();
    
    // forward the event from embeddable beans to all listeners. So the parent of the embeddable
    // bean will become notified and its dirty state can be handled properly
    { 
    	// no super class available to forward event
    }
  }
}
