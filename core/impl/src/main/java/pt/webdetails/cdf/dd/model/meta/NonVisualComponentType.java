/*! ******************************************************************************
 *
 * Pentaho
 *
 * Copyright (C) 2024 by Hitachi Vantara, LLC : http://www.pentaho.com
 *
 * Use of this software is governed by the Business Source License included
 * in the LICENSE.TXT file.
 *
 * Change Date: 2029-07-20
 ******************************************************************************/


package pt.webdetails.cdf.dd.model.meta;

import pt.webdetails.cdf.dd.model.core.validation.ValidationException;

public abstract class NonVisualComponentType extends ComponentType {
  protected NonVisualComponentType( Builder builder, IPropertyTypeSource propSource )
    throws ValidationException {
    super( builder, propSource );
  }

  public static abstract class Builder extends ComponentType.Builder {
    @Override
    public abstract NonVisualComponentType build( IPropertyTypeSource propSource )
      throws ValidationException;
  }
}
