// Copyright (c) 2010-2011 Amazon.com, Inc.  All rights reserved.

package com.amazon.ion.impl.lite;

import static com.amazon.ion.impl.IonReaderFactoryX.makeReader;

import com.amazon.ion.IonCatalog;
import com.amazon.ion.IonDatagram;
import com.amazon.ion.IonException;
import com.amazon.ion.IonLoader;
import com.amazon.ion.IonReader;
import com.amazon.ion.IonSystem;
import com.amazon.ion.IonWriter;
import com.amazon.ion.impl.IonReaderFactoryX;
import com.amazon.ion.impl.IonWriterFactory;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

/**
 *
 */
public class IonLoaderLite
    implements IonLoader
{
    private final IonSystemLite _system;
    private final IonCatalog    _catalog;

    public IonLoaderLite(IonSystemLite system, IonCatalog catalog)
    {
        _system = system;
        _catalog = catalog;
    }

    public IonSystem getSystem()
    {
        return _system;
    }

    /**
     * This doesn't wrap IOException because some callers need to propagate it.
     */
    private IonDatagramLite load_helper(IonReader reader)
    throws IOException
    {
        IonDatagramLite datagram = new IonDatagramLite(_system, _catalog);
        IonWriter writer = IonWriterFactory.makeWriter(datagram);
        writer.writeValues(reader);
        datagram.populateSymbolValues(null);
        return datagram;
    }

    public IonDatagram load(File ionFile) throws IonException, IOException
    {
        InputStream ionData = new FileInputStream(ionFile);
        try
        {
            IonDatagram datagram = load(ionData);
            return datagram;
        }
        finally
        {
            ionData.close();
        }
    }

    public IonDatagram load(String ionText) throws IonException
    {
        try {
            IonReader reader = makeReader(_system, _catalog, ionText);
            IonDatagramLite datagram = load_helper(reader);
            return datagram;
        }
        catch (IOException e) {
            throw new IonException(e);
        }
    }

    public IonDatagram load(Reader ionText) throws IonException, IOException
    {
        IonReader reader = makeReader(_system, _catalog, ionText);
        IonDatagramLite datagram = load_helper(reader);
        return datagram;
    }

    public IonDatagram load(byte[] ionData) throws IonException
    {
        try {
            IonReader reader = makeReader(_system, _catalog, ionData, 0, ionData.length);
            IonDatagramLite datagram = load_helper(reader);
            return datagram;
        }
        catch (IOException e) {
            throw new IonException(e);
        }
    }

    public IonDatagram load(InputStream ionData)
        throws IonException, IOException
    {
        IonReader reader = IonReaderFactoryX.makeReader(_system, _catalog, ionData);
        IonDatagramLite datagram = load_helper(reader);
        return datagram;
    }

}