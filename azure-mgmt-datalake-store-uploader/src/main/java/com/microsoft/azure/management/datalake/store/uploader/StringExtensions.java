/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See License.txt in the project root for
 * license information.
 */
package com.microsoft.azure.management.datalake.store.uploader;

import org.apache.commons.lang3.StringUtils;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * Created by begoldsm on 4/11/2016.
 */
public class StringExtensions {
    /// <summary>
    /// Finds the index in the given buffer of a newline character, either the first or the last (based on the parameters).
    /// If a combined newline (\r\n), the index returned is that of the last character in the sequence.
    /// </summary>
    /// <param name="buffer">The buffer to search in.</param>
    /// <param name="startOffset">The index of the first byte to start searching at.</param>
    /// <param name="length">The number of bytes to search, starting from the given startOffset.</param>
    /// <param name="reverse">If true, searches from the startOffset down to the beginning of the buffer. If false, searches upwards.</param>
    /// <returns>The index of the closest newline character in the sequence (based on direction) that was found. Returns -1 if not found. </returns>
    public static int FindNewline(byte[] buffer, int startOffset, int length, boolean reverse, Charset encoding, String delimiter) {
        if (buffer.length == 0 || length == 0) {
            return -1;
        }

        // define the bytes per character to use
        int bytesPerChar;
        if (encoding.equals(StandardCharsets.UTF_16) || encoding.equals(StandardCharsets.UTF_16BE) || encoding.equals(StandardCharsets.UTF_16LE)) {
            bytesPerChar = 2;
        } else if (encoding.equals(StandardCharsets.US_ASCII) || encoding.equals(StandardCharsets.UTF_8)) {
            bytesPerChar = 1;
        } else {
            throw new IllegalArgumentException("Only the following encodings are allowed: UTF-8, UTF-16, UTF-16BE, UTF16-LE and ASCII");
        }


        if (delimiter != null && !StringUtils.isEmpty(delimiter) && delimiter.length() > 1) {
            throw new IllegalArgumentException("The delimiter must only be a single character or unspecified to represent the CRLF delimiter");
        }

        if (delimiter != null && !StringUtils.isEmpty(delimiter)) {
            // convert the byte array back to a String
            int startOfSegment = reverse ? startOffset - length + 1 : startOffset;
            String bytesToString = new String(buffer, startOfSegment, length, encoding);
            if (!bytesToString.contains(delimiter)) {
                // didn't find the delimiter.
                return -1;
            }

            // the index is returned, which is 0 based, so our loop must include the zero case.
            int numCharsToDelim = reverse ? bytesToString.lastIndexOf(delimiter) : bytesToString.indexOf(delimiter);
            int toReturn = 0;
            for (int i = 0; i <= numCharsToDelim; i++) {
                toReturn += Character.toString(bytesToString.charAt(startOfSegment + i)).getBytes(encoding).length;
            }

            // we get the total number of bytes, but we want to return the index (which starts at 0)
            // so we subtract 1 from the total number of bytes to get the final byte index.
            return toReturn - 1;
        }

        //endOffset is a 'sentinel' value; we use that to figure out when to stop searching 
        int endOffset = reverse ? startOffset - length : startOffset + length;

        // if we are starting at the end, we need to move toward the front enough to grab the right number of bytes
        startOffset = reverse ? startOffset - (bytesPerChar - 1) : startOffset;

        if (startOffset < 0 || startOffset >= buffer.length) {
            throw new IndexOutOfBoundsException("Given start offset is outside the bounds of the given buffer. In reverse cases, the start offset is modified to ensure we check the full size of the last character");
        }

        // make sure that the length we are traversing is at least as long as a single character
        if (length < bytesPerChar) {
            throw new IllegalArgumentException("Length must be at least as long as the length, in bytes, of a single character");
        }

        if (endOffset < -1 || endOffset > buffer.length) {
            throw new IndexOutOfBoundsException("Given combination of startOffset and length would execute the search outside the bounds of the given buffer.");
        }

        int bufferEndOffset = reverse ? startOffset : startOffset + length;
        int result = -1;
        for (int charPos = startOffset; reverse ? charPos != endOffset : charPos + bytesPerChar - 1 < endOffset; charPos = reverse ? charPos - 1 : charPos + 1) {
            char c = bytesPerChar == 1 ? (char) buffer[charPos] : new String(buffer, charPos, bytesPerChar, encoding).toCharArray()[0];
            if (IsNewline(c, delimiter)) {
                result = charPos + bytesPerChar - 1;
                break;
            }
        }

        if ((delimiter == null || StringUtils.isEmpty(delimiter)) && !reverse && result < bufferEndOffset - bytesPerChar && IsNewline(bytesPerChar == 1 ? (char) buffer[result + bytesPerChar] : new String(buffer, result + 1, bytesPerChar, encoding).toCharArray()[0], delimiter)) {
            //we originally landed on a \r character; if we have a \r\n character, advance one position to include that
            result += bytesPerChar;
        }

        return result;
    }

    /// <summary>
    /// Determines whether the specified character is newline.
    /// </summary>
    /// <param name="c">The character.</param>
    /// <returns></returns>
    private static boolean IsNewline(char c, String delimiter) {
        if ((delimiter == null || StringUtils.isEmpty(delimiter))) {
            return c == '\r' || c == '\n';
        }

        return c == delimiter.toCharArray()[0];
    }
}
