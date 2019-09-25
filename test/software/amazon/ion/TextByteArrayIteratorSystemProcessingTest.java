/*
 * Copyright 2007-2019 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * You may not use this file except in compliance with the License.
 * A copy of the License is located at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * or in the "license" file accompanying this file. This file is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */

package software.amazon.ion;

import software.amazon.ion.impl._Private_Utils;
import java.util.Iterator;


public class TextByteArrayIteratorSystemProcessingTest
    extends IteratorSystemProcessingTestCase
{
    private byte[] myBytes;

    @Override
    protected void prepare(String text)
        throws Exception
    {
        myBytes = _Private_Utils.convertUtf16UnitsToUtf8(text);
    }

    @Override
    protected Iterator<IonValue> iterate()
    {
        return system().iterate(myBytes);
    }

    @Override
    protected Iterator<IonValue> systemIterate()
    {
        return system().systemIterate(system().newSystemReader(myBytes));
    }
}