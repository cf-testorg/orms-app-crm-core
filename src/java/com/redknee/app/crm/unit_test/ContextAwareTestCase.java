/*
 * This code is a protected work and subject to domestic and international
 * copyright law(s).  A complete listing of authors of this work is readily
 * available.  Additionally, source code is, by its very nature, confidential
 * information and inextricably contains trade secrets and other information
 * proprietary, valuable and sensitive to Redknee.  No unauthorized use,
 * disclosure, manipulation or otherwise is permitted, and may only be used
 * in accordance with the terms of the license agreement entered into with
 * Redknee Inc. and/or its subsidiaries.
 *
 * Copyright (c) Redknee Inc. and its subsidiaries. All Rights Reserved.
 */
package com.redknee.app.crm.unit_test;

import junit.framework.TestCase;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

import com.redknee.framework.license.DefaultLicenseMgr;
import com.redknee.framework.license.LicenseHome;
import com.redknee.framework.license.LicenseMgr;
import com.redknee.framework.license.LicenseTransientHome;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.context.ContextAware;
import com.redknee.framework.xhome.context.ContextAwareSupport;
import com.redknee.framework.xhome.context.ContextLocator;
import com.redknee.framework.xhome.context.ContextSupport;
import com.redknee.framework.xhome.home.Home;

import com.redknee.app.crm.xhome.home.TransientFieldResettingHome;

/**
 * Provides a base for creating JUnit TestCase implementations that are context aware.
 * Derived classes should call setParentContext() to set the parent context from which
 * subcontexts will be created. The setUp() method creates the subcontext returned by
 * getContext(), and the tearDown() discards that subcontext.
 *
 * @author gary.anderson@redknee.com
 * @author cindy.wong@redknee.com
 */
public class ContextAwareTestCase extends TestCase implements ContextAware
{

    /**
     * Create a new instance of <code>ContextAwareTestCase</code>.
     */
    public ContextAwareTestCase()
    {
        super();
        this.context_ = null;
    }

    /**
     * Constructs a test case with the given name.
     *
     * @param name
     *            The name of the test.
     */
    public ContextAwareTestCase(final String name)
    {
        super(name);
        this.context_ = null;
    }


    /**
     * {@inheritDoc}
     */
    public Context getContext()
    {
        return this.context_;
    }


    /**
     * {@inheritDoc}
     */
    public void setContext(final Context context)
    {
        this.context_ = context;
    }

    
    /**
     * This will execute exactly once before the tests in the class have been run
     * (As of JUnit 4)
     */
    @BeforeClass
    protected void globalSetUp()
    {
    }
    
    /**
     * This will execute before each test case is run
     */
    @Override
    @Before
    protected void setUp()
    {
        if (CONTEXT_SUPPORT.getContext() == null )
        {
            CONTEXT_SUPPORT.setContext(new ContextSupport());
        }
        
        if (ContextLocator.locate() == null)
        {
            ContextLocator.setThreadContext(CONTEXT_SUPPORT.getContext());
        }

        final Context context = CONTEXT_SUPPORT.getContext().createSubContext(this.getClass().getName());

        
        final Home licenses = new TransientFieldResettingHome(context, new LicenseTransientHome(context));
        context.put(LicenseHome.class, licenses);
        context.put(LicenseMgr.class, new DefaultLicenseMgr(context));
        
        context_ = context;        
    }
    
    /**
     * This will execute exactly once after all tests in the class have been run
     * (As of JUnit 4)
     */
    @AfterClass
    protected void globalTearDown()
    {
    }


    /**
     * Tears down test case.
     */
    @Override
    @After
    protected void tearDown()
    {
        this.context_ = null;
    }


    /**
     * Sets the parent context to be used.
     *
     * @param context
     *            The operating context.
     */
    protected static void setParentContext(final Context context)
    {
        CONTEXT_SUPPORT.setContext(context);
    }

    /**
     * Provides ContextAware support for this class. ContextAwareSupport is abstract, so
     * we must create a derivation of it. The context kept by this support is not used
     * directly, but rather used as a parent in the setUp() and tearDown() methods.
     */
    private static final ContextAware CONTEXT_SUPPORT = new ContextAwareSupport()
    {
        // empty
    };

    /**
     * The operating context used in the test methods.
     */
    private Context context_;
} // class
