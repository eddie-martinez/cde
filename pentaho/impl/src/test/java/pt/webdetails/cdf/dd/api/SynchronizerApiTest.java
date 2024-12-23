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

package pt.webdetails.cdf.dd.api;

import org.apache.commons.lang.StringUtils;
import org.json.JSONObject;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import pt.webdetails.cpf.messaging.MockHttpServletRequest;
import pt.webdetails.cpf.messaging.MockHttpServletResponse;
import pt.webdetails.cpf.utils.CharsetHelper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON_TYPE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doReturn;

public class SynchronizerApiTest {

  private SyncronizerApi synchronizerApi;
  private MockHttpServletRequest servletRequest;
  private MockHttpServletResponse servletResponse;

  private String file;
  private String path;

  private static final String title = "MOCK_TITLE";
  private static final String author = "MOCK_AUTHOR";
  private static final String description = "MOCK_DESCRIPTION";
  private static final String style = "MOCK_STYLE";
  private static final String widgetName = "MOCK_WIDGET_NAME";
  private static final boolean widget = false;
  private static final boolean require = false;
  private static final String rendererType = "MOCK_RENDERED_TYPE";
  private static final List<String> widgetParams = new ArrayList<>();
  private static final String cdfStructure = "MOCK_CDF_STRUCTURE";

  private String operation = "savesettings";

  private static XSSHelper originalHelper;
  private static XSSHelper mockHelper;

  @BeforeClass
  public static void setUp() {
    originalHelper = XSSHelper.getInstance();
  }

  @AfterClass
  public static void afterAll() {
    XSSHelper.setInstance( originalHelper );
  }

  @Before
  public void beforeEach() throws Exception {
    synchronizerApi = spy( new SynchronizerApiForTesting() );
    doReturn(true).when(synchronizerApi).isAllowSaveDashboard();

    servletRequest = new MockHttpServletRequest( "/pentaho-cdf/api/views", new HashMap<>() );

    servletResponse = new MockHttpServletResponse( new ObjectOutputStream( new ByteArrayOutputStream() ) );
    servletResponse.setContentType( null );
    servletResponse.setCharacterEncoding( null );

    path = StringUtils.EMPTY;
    file = StringUtils.EMPTY;

    mockHelper = mock( XSSHelper.class );
    when( mockHelper.escape( any() ) ).thenAnswer( invocation -> invocation.getArguments()[ 0 ] );
    XSSHelper.setInstance( mockHelper );
  }

  @After
  public void tearDown() {
    synchronizerApi = null;
    servletRequest = null;
    servletResponse = null;
    file = null;
    path = null;

    reset( mockHelper );
  }

  @Test
  public void saveSettingsWithoutDoingInitialDashboardSaveTest() throws Exception {
    path = StringUtils.EMPTY;
    file = StringUtils.EMPTY; // no file sent, therefore no initial dashboard save has been done

    HttpServletRequest mockRequest = mock( HttpServletRequest.class );
    when( mockRequest.getParameterMap() ).thenReturn( new HashMap<>() );

    HttpServletResponse mockResponse = mock( HttpServletResponse.class );

    String result = new SynchronizerApiForTesting()
      .syncronize( file, path, title, author, description, style, widgetName, widget, rendererType, widgetParams,
        cdfStructure, operation, require, mockRequest, mockResponse );

    JSONObject jsonObj = new JSONObject( result );

    assertNotNull( jsonObj.getString( "status" ) );
    assertNotNull( jsonObj.getString( "result" ) );

    assertEquals( "false", jsonObj.getString( "status" ) );
    assertEquals( "CdfTemplates.ERROR_003_SAVE_DASHBOARD_FIRST", jsonObj.getString( "result" ) );

    verify( mockHelper, atLeastOnce() ).escape( any() );
  }

  @Test
  public void listViewsTest() throws Exception {
    assertNull( servletResponse.getContentType() );
    assertNull( servletResponse.getCharacterEncoding() );

    synchronizerApi.syncronize( file, path, title, author, description, style, widgetName, widget, rendererType,
      widgetParams, cdfStructure, operation, require, servletRequest, servletResponse );

    assertEquals( APPLICATION_JSON, servletResponse.getContentType() );
    assertEquals( CharsetHelper.getEncoding(), servletResponse.getCharacterEncoding() );

    verify( mockHelper, atLeastOnce() ).escape( any() );
  }

  @Test
  public void syncTemplatesTest() throws Exception {
    operation = StringUtils.EMPTY;

    assertNull( servletResponse.getContentType() );
    assertNull( servletResponse.getCharacterEncoding() );

    synchronizerApi.syncTemplates( operation, file, cdfStructure, rendererType, servletResponse );

    assertEquals( APPLICATION_JSON, servletResponse.getContentType() );
    assertEquals( CharsetHelper.getEncoding(), servletResponse.getCharacterEncoding() );

    verify( mockHelper, atLeastOnce() ).escape( any() );
  }

  @Test
  public void syncStylesTest() throws Exception {
    final Response response = synchronizerApi.syncStyles();

    Map<String, String> mtParameters = new HashMap<>();
    mtParameters.put( "charset", CharsetHelper.getEncoding() );

    MediaType mt = new MediaType( APPLICATION_JSON_TYPE.getType(), APPLICATION_JSON_TYPE.getSubtype(), mtParameters );

    assertEquals( mt, response.getMediaType() );
  }

  @Test
  public void saveDashboardTest() throws Exception {
    operation = StringUtils.EMPTY;

    assertNull( servletResponse.getContentType() );
    assertNull( servletResponse.getCharacterEncoding() );

    synchronizerApi.saveDashboard( file, title, description, cdfStructure, operation, servletResponse );

    assertEquals( APPLICATION_JSON, servletResponse.getContentType() );
    assertEquals( CharsetHelper.getEncoding(), servletResponse.getCharacterEncoding() );

    verify( mockHelper, atLeastOnce() ).escape( any() );
  }

  @Test
  public void testSaveDashboardWhenAllowSaveDashboardIsFalse() throws Exception {

    doReturn(false).when(synchronizerApi).isAllowSaveDashboard();
    operation = SyncronizerApi.OPERATION_SAVE;

    assertNull( servletResponse.getContentType() );
    assertNull( servletResponse.getCharacterEncoding() );

    synchronizerApi.saveDashboard( file, title, description, cdfStructure, operation, servletResponse );
    assertEquals( APPLICATION_JSON, servletResponse.getContentType() );
    assertEquals( CharsetHelper.getEncoding(), servletResponse.getCharacterEncoding() );
    verify( mockHelper, times(0) ).escape( any() );
  }

}
