
package org.crosswire.jsword.passage;

import java.io.Serializable;
import java.util.Iterator;

/**
 * The base unit that is collected by a Passage.
 *
 * <table border='1' cellPadding='3' cellSpacing='0' width="100%">
 * <tr><td bgColor='white'class='TableRowColor'><font size='-7'>
 * Distribution Licence:<br />
 * Project B is free software; you can redistribute it
 * and/or modify it under the terms of the GNU General Public License,
 * version 2 as published by the Free Software Foundation.<br />
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.<br />
 * The License is available on the internet
 * <a href='http://www.gnu.org/copyleft/gpl.html'>here</a>, by writing to
 * <i>Free Software Foundation, Inc., 59 Temple Place - Suite 330, Boston,
 * MA 02111-1307, USA</i>, Or locally at the Licence link below.<br />
 * The copyright to this program is held by it's authors.
 * </font></td></tr></table>
 * @see <a href='http://www.eireneh.com/servlets/Web'>Project B Home</a>
 * @see <{docs.Licence}>
 * @author Joe Walker
 * @version $Id$
 */
public interface VerseBase extends Cloneable, Comparable, Serializable, PassageConstants
{
    /**
     * Translate the Passage into a human readable string
     * @return The string representation
     */
    public String getName();

    /**
     * Translate the Passage into a human readable string, with the
     * assumption that the specified Verse has just been output, so if we
     * are in the same book, we do not need to display the book name, and
     * so on.
     * @param base The verse to use to cut down unnecessary output.
     * @return The string representation
     */
    public String getName(Verse base);

    /**
     * Create an array of Verses.
     * See note on verseElements()
     * @return The array of verses that this makes up
     * @see #verseIterator()
     */
    public Verse[] toVerseArray();

    /**
     * Enumerate over the verses in this object. I remember thinking at some
     * stage that I ought to just use one of toVerseArray() and verseElements()
     * and contemplated removing the other one, but didn't make the change. I
     * suspect the newer (and therefore probably better) implementation is going
     * to be further down the file (i.e. this one), and so toVerseArray should
     * not be used anymore. However I can't remember the reasoning behind it
     * other than the possibility of less Object generation if you are not
     * going to itterate over the whole array.
     * @return A verse iterator
     */
    public Iterator verseIterator();
}