package org.lunifera.dsl.ext.sample.cpp.qt;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import org.lunifera.dsl.dto.lib.MappingContext;
import org.lunifera.dsl.ext.sample.cpp.qt.MyEntity;
import org.lunifera.runtime.common.annotations.Dispose;
import org.lunifera.runtime.common.annotations.DomainReference;

@SuppressWarnings("all")
public class MyOther implements Serializable, PropertyChangeListener {
  private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);
  
  @Dispose
  private boolean disposed;
  
  @DomainReference
  private MyEntity entity;
  
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
   * Returns the entity property or <code>null</code> if not present.
   */
  public MyEntity getEntity() {
    return this.entity;
  }
  
  /**
   * Sets the <code>entity</code> property to this instance.
   * Since the reference has an opposite reference, the opposite <code>MyEntity#
   * others</code> of the <code>entity</code> will be handled automatically and no 
   * further coding is required to keep them in sync.<p>
   * See {@link MyEntity#setOthers(MyEntity)
   * 
   * @param entity - the property
   * @throws RuntimeException if instance is <code>disposed</code>
   * 
   */
  public void setEntity(final MyEntity entity) {
    checkDisposed();
    if (this.entity != null) {
    	this.entity.internalRemoveFromOthers(this);
    }
    
    internalSetEntity(entity);
    
    if (this.entity != null) {
    	this.entity.internalAddToOthers(this);
    }
  }
  
  /**
   * For internal use only!
   */
  void internalSetEntity(final MyEntity entity) {
    firePropertyChange("entity", this.entity, this.entity = entity);
  }
  
  public MyOther createDto() {
    return new MyOther();
  }
  
  public MyOther copy(final MappingContext context) {
    checkDisposed();
    
    if (context == null) {
    	throw new IllegalArgumentException("Context must not be null!");
    }
    
    if(context.isMaxLevel()){
    	return null;
    }
    
    // if context contains a copied instance of this object
    // then return it
    MyOther newDto = context.get(this);
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
  
  public void copyContainments(final MyOther dto, final MyOther newDto, final MappingContext context) {
    checkDisposed();
    
    if (context == null) {
    	throw new IllegalArgumentException("Context must not be null!");
    }
    
    
    // copy attributes and beans (beans if derived from entity model)
    
    // copy containment references (cascading is true)
  }
  
  public void copyCrossReferences(final MyOther dto, final MyOther newDto, final org.lunifera.dsl.dto.lib.MappingContext context) {
    checkDisposed();
    
    if (context == null) {
    	throw new IllegalArgumentException("Context must not be null!");
    }
    
    
    // copy cross references (cascading is false)
    // copy dto entity
    if(getEntity() != null) {
    	newDto.setEntity(getEntity().copy(context));
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
