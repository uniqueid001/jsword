
package org.crosswire.jsword.book;

import java.util.Iterator;
import java.util.ArrayList;

import org.crosswire.jsword.passage.Passage;
import org.crosswire.jsword.passage.PassageFactory;
import org.crosswire.jsword.passage.PassageTally;
import org.crosswire.jsword.passage.PassageUtil;

/**
 * The BookUtil class provide utility functions for the various Books.
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
public class BookUtil
{
    /**
     * Ensure we can not be instansiated
     */
    private BookUtil()
    {
    }

    /**
     * A basic version of getPassageTally(String[]) simply calls
     * getPassage(String) in a loop for each word, adding the Verses
     * to an PassageTally that is returned
     * @param version The version to search using
     * @param tally The PassageTally to update
     * @param words The words to search for
     * @throws BookException If anything goes wrong with this method
     */
    public static void updatePassageTally(Bible version, PassageTally tally, String[] words) throws BookException
    {
        for (int i=0; i<words.length; i++)
        {
            tally.addAll(version.findPassage(words[i]));
        }
    }

    /**
     * This is similar to updatePassageTally() however if a verse matches
     * many words it still only adds on for that verse in the given tally
     * @param version The version to search using
     * @param tally The PassageTally to update
     * @param words The words to search for
     * @throws BookException If anything goes wrong with this method
     */
    public static void updatePassageTallyFlat(Bible version, PassageTally tally, String[] words) throws BookException
    {
        PassageTally temp = new PassageTally();

        for (int i=0; i<words.length; i++)
        {
            temp.addAll(version.findPassage(words[i]));
        }

        temp.flatten();
        tally.addAll(temp);
    }

    /**
     * A basic version of getPassage(String[]) simply calls
     * getPassage(String) in a loop for each word, adding the Verses
     * to an Passage that is returned
     * @param version The version to search using
     * @param words The words to search for
     * @return The Passage
     * @throws BookException If anything goes wrong with this method
     */
    public static Passage getPassage(Bible version, String[] words) throws BookException
    {
        Passage ref = PassageFactory.createPassage();

        for (int i=0; i<words.length; i++)
        {
            ref.addAll(version.findPassage(words[i]));
        }

        return ref;
    }

    /**
     * Take a string and tokenize it using " " and "--" as delimiters
     * into an Array of Strings. There is a question mark over what to do
     * with initial spaces. This algorithm disgards them, I'm not sure if
     * this is the right thing to do.
     * @param sentance The string to parse.
     * @return The string array
     */
    public static String[] tokenize(String sentance)
    {
        ArrayList tokens = new ArrayList();

        int pos = 0;
        String temp;
        boolean alive = true;

        while (alive)
        {
            // Find the next space and double dash
            int next_space = sentance.indexOf(" ", pos);
            int next_ddash = sentance.indexOf("--", pos);

            // If there is a space just after the ddash then ignore the ddash
            if (next_space == next_ddash + 2)
            {
                next_ddash = -1;
            }

            // If there is a ddash just after the space then ignore the space
            if (next_ddash == next_space + 1)
            {
                next_space = -1;
            }

            // if there are no more tokens then just add in what we've got.
            if (next_space == -1 && next_ddash == -1)
            {
                temp = sentance.substring(pos);
                alive = false;
            }
            // Space is next if it is not -1 and it is less than ddash
            else if ((next_space != -1 && next_space < next_ddash) ||
                    (next_ddash == -1))
            {
                // The next separator is a space
                temp = sentance.substring(pos, next_space) + " ";
                pos = next_space + 1;
            }
            else
            {
                // The next separator is a ddash
                temp = sentance.substring(pos, next_ddash) + "--";
                pos = next_ddash + 2;
            }

            if (temp != null && !temp.trim().equals(""))
                tokens.add(temp);
        }

        // Create a String[]
        String[] retcode = new String[tokens.size()];
        int i = 0;
        for (Iterator it = tokens.iterator(); it.hasNext();)
        {
            retcode[i++] = (String) it.next();
        }

        return retcode;
    }

    /**
     * From a sentance get a list of words (in original order) without
     * any punctuation, and all in lower case.
     * @param sentance The string to parse.
     * @return The words split up as an array
     */
    public static String[] getWords(String sentance)
    {
        String words[] = tokenize(sentance);
        String[] retcode = new String[words.length];

        // Remove the punctuation from the ends of the words.
        for (int i=0; i<words.length; i++)
        {
            retcode[i] = stripPunctuationWord(words[i]).toLowerCase();
        }

        return retcode;
    }

    /**
     * From a sentance get a list of words (in original order) without
     * any punctuation, and all in lower case.
     * @param words Words with punctuation
     * @return Words without punctuation
     */
    public static String[] stripPunctuation(String[] words)
    {
        String[] retcode = new String[words.length];

        // Remove the punctuation from the ends of the words.
        for (int i=0; i<words.length; i++)
        {
            retcode[i] = stripPunctuationWord(words[i]);
        }

        return retcode;
    }

    /**
     * Remove the punctuation from the ends of the word
     * @param word Word with punctuation
     * @return Word without punctuation
     */
    protected static String stripPunctuationWord(String word)
    {
        int first = firstLetter(word);
        int last = lastLetter(word)+1;

        if (first > last) return word;

        return word.substring(first, last);
    }

    /**
     * From a sentance get a list of words (in original order) without
     * any punctuation, and all in lower case.
     * @param words Words with punctuation
     * @return Punctuation without words
     */
    public static String[] stripWords(String[] words)
    {
        if (words.length == 0)
            return new String[0];

        String[] retcode = new String[words.length+1];

        // The first bit of punctuation is what comes in front of the first word
        int first = firstLetter(words[0]);
        if (first == 0) retcode[0] = "";
        else            retcode[0] = words[0].substring(0, first);

        // The rest of the words
        for (int i=1; i<words.length; i++)
        {
            retcode[i] = stripWords(words[i-1], words[i]);
        }

        // The last bit of punctuation is what comes at the end of the last word
        int last = lastLetter(words[words.length-1]);
        if (last == words[words.length-1].length())
            retcode[words.length] = "";
        else
            retcode[words.length] = words[words.length-1].substring(last+1);

        return retcode;
    }

    /**
     * Remove the punctuation from the ends of the word. The special
     * case is that if the first word ends "--" and the last word has
     * no punctuation at the beginning, then the answer is "--" and not
     * "-- ". We miss out the space because "--" is a special separator.
     * @param first The word to grab the punctuation from the end of
     * @param last The word to grab the punctuation from the start of
     * @return The end of the first, a space, and the end of the first
     */
    protected static String stripWords(String first, String last)
    {
        String init1 = first.substring(lastLetter(first)+1);
        String init2 = last.substring(0, firstLetter(last));

        return init1 + init2;
    }

    /**
     * From a sentance get a list of words (in original order) without
     * any punctuation, and all in lower case.
     * @param words an array of words to find punctuation from
     * @return Array of case definitions
     */
    public static int[] getCases(String[] words)
    {
        int[] retcode = new int[words.length];

        // Remove the punctuation from the ends of the words.
        for (int i=0; i<words.length; i++)
        {
            retcode[i] = PassageUtil.getCase(words[i]);
        }

        return retcode;
    }

    /**
     * Where is the first letter in this word
     * @param word The word to search for letters
     * @return The offset of the first letter
     */
    protected static final int firstLetter(String word)
    {
        int first;

        for (first=0; first<word.length(); first++)
        {
            char c = word.charAt(first);
            if (Character.isLetterOrDigit(c))
                break;
        }

        return first;
    }

    /**
     * Where is the last letter in this word
     * @param word The word to search for letters
     * @return The offset of the last letter
     */
    protected static final int lastLetter(String word)
    {
        int last;

        for (last=word.length()-1; last>=0; last--)
        {
            char c = word.charAt(last);
            if (Character.isLetterOrDigit(c))
                break;
        }

        return last;
    }

    /**
     * Convert an Iterator of strings to an array of strings. Needed because
     * quite a few things want to take the result of Book.startsWith() and
     * pass it into a method taking String[]
     * @param it The Iterator to convert
     * @return A string array
     * @throws ClassCastException if the iterator isn't 100% strings
     */
    public static String[] toStringArray(Iterator it) throws ClassCastException
    {
        ArrayList list = new ArrayList();
        while (it.hasNext())
        {
            String s = (String) it.next();
            list.add(s);
        }

        int len = list.size();
        String[] array = new String[len];
        for (int i=0; i<array.length; i++)
        {
            array[i] = (String) list.get(i);
        }

        return array;
    }
}