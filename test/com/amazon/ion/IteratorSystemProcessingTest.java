// Copyright (c) 2008-2011 Amazon.com, Inc.  All rights reserved.

package com.amazon.ion;

import static com.amazon.ion.SymbolTable.UNKNOWN_SYMBOL_ID;

import com.amazon.ion.impl.IonSystemPrivate;
import java.util.Arrays;
import java.util.Iterator;
import org.junit.Assert;

/**
 *
 */
public class IteratorSystemProcessingTest
    extends SystemProcessingTestCase
{
    private String myText;
    private Iterator<IonValue> myIterator;
    private IonValue myCurrentValue;


    protected Iterator<IonValue> iterate()
        throws Exception
    {
        return system().iterate(myText);
    }

    protected Iterator<IonValue> systemIterate()
        throws Exception
    {
        IonSystemPrivate sys = system();
        Iterator<IonValue> it = sys.systemIterate(myText);
        return it;
    }


    @Override
    protected void prepare(String text)
        throws Exception
    {
        myText = text;
    }

    @Override
    protected void startIteration() throws Exception
    {
        myIterator = iterate();
    }

    @Override
    protected void startSystemIteration() throws Exception
    {
        myIterator = systemIterate();
    }

    @Override
    protected void nextValue() throws Exception
    {
        myCurrentValue = myIterator.next();
    }

    @Override
    protected void stepIn() throws Exception
    {
        myIterator = ((IonContainer)myCurrentValue).iterator();
    }

    @Override
    protected void stepOut() throws Exception
    {

    }

    @Override
    protected IonType currentValueType() throws Exception
    {
        if (myCurrentValue == null) {
            return null;
        }
        return myCurrentValue.getType();
    }


    /**
     * @param expectedText null means absent
     */
    @Override
    final void checkFieldName(String expectedText, int expectedSid)
        throws Exception
    {
        String expectedStringValue =
            (expectedText == null ? "$" + expectedSid : expectedText);
        assertEquals("IonValue.getFieldName()",
                     expectedStringValue, myCurrentValue.getFieldName());

        assertEquals("IonValue.getFieldId",
                     expectedSid, myCurrentValue.getFieldId());

        InternedSymbol sym = myCurrentValue.getFieldNameSymbol();
        checkSymbol(expectedText, expectedSid, sym);
        assertEquals(expectedText, sym.getText());
        assertEquals(expectedSid,  sym.getId());
    }



    @Override
    protected void checkAnnotation(String expected, int expectedSid)
    {
        if (! myCurrentValue.hasTypeAnnotation(expected))
        {
            fail("Didn't find expected annotation: " + expected);
        }

        String[] typeAnnotations = myCurrentValue.getTypeAnnotations();
        if (! Arrays.asList(typeAnnotations).contains(expected))
        {
            fail("Didn't find expected annotation: " + expected);
        }

        checkSymbol(expected, expectedSid, myCurrentValue.getSymbolTable());
    }

    @Override
    protected void checkAnnotations(String[] expecteds,
                                    int[] expectedSids)
    {
        String[] typeAnnotations = myCurrentValue.getTypeAnnotations();
        Assert.assertArrayEquals(expecteds, typeAnnotations);

        SymbolTable symtab = myCurrentValue.getSymbolTable();
        for (int i = 0; i < expecteds.length; i++)
        {
            int foundSid = symtab.findSymbol(expecteds[i]);
            if (foundSid != UNKNOWN_SYMBOL_ID)
            {
                assertEquals("symbol id", expectedSids[i], foundSid);
            }
        }
    }

    @Override
    protected void checkType(IonType expected)
    {
        assertSame(expected, myCurrentValue.getType());
    }

    @Override
    protected void checkInt(long expected) throws Exception
    {
        checkInt(expected, myCurrentValue);
    }

    @Override
    protected void checkDecimal(double expected) throws Exception
    {
        checkDecimal(expected, myCurrentValue);
    }

    @Override
    protected void checkFloat(double expected) throws Exception
    {
        checkFloat(expected, myCurrentValue);
    }

    @Override
    protected void checkString(String expected) throws Exception
    {
        checkString(expected, myCurrentValue);
    }

    @Override
    protected void checkSymbol(String expected) throws Exception
    {
        checkSymbol(expected, myCurrentValue);
    }

    @Override
    protected void checkSymbol(String expected, int expectedSid)
        throws Exception
    {
        checkSymbol(expected, expectedSid, myCurrentValue);
    }


    @Override
    protected void checkTimestamp(Timestamp expected) throws Exception
    {
        checkTimestamp(expected, myCurrentValue);
    }

    @Override
    SymbolTable currentSymtab()
    {
        return myCurrentValue.getSymbolTable();
    }

    @Override
    protected void checkEof() throws Exception
    {
        if (myIterator.hasNext())
        {
            fail("expected EOF, found " +  myIterator.next());
        }
    }

    @Override
    protected void testString(String expectedValue,
                              String expectedRendering,
                              String ionData)
        throws Exception
    {
        super.testString(expectedValue, expectedRendering, ionData);
        assertEquals(expectedRendering, myCurrentValue.toString());
    }
}
