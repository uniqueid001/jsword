package org.crosswire.jsword.passage;

import java.util.Iterator;

/**
 * An implementation of some of the easier methods from Key.
 * 
 * <p><table border='1' cellPadding='3' cellSpacing='0'>
 * <tr><td bgColor='white' class='TableRowColor'><font size='-7'>
 *
 * Distribution Licence:<br />
 * JSword is free software; you can redistribute it
 * and/or modify it under the terms of the GNU General Public License,
 * version 2 as published by the Free Software Foundation.<br />
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.<br />
 * The License is available on the internet
 * <a href='http://www.gnu.org/copyleft/gpl.html'>here</a>, or by writing to:
 * Free Software Foundation, Inc., 59 Temple Place - Suite 330, Boston,
 * MA 02111-1307, USA<br />
 * The copyright to this program is held by it's authors.
 * </font></td></tr></table>
 * @see gnu.gpl.Licence
 * @author Joe Walker [joe at eireneh dot com]
 * @version $Id$
 */
public abstract class AbstractKeyList implements Key
{
    /* (non-Javadoc)
     * @see org.crosswire.jsword.passage.Key#isEmpty()
     */
    public boolean isEmpty()
    {
        return getChildCount() == 0;
    }

    /* (non-Javadoc)
     * @see org.crosswire.jsword.passage.Key#contains(org.crosswire.jsword.passage.Key)
     */
    public boolean contains(Key key)
    {
        for (Iterator it = iterator(); it.hasNext(); )
        {
            Key temp = (Key) it.next();
            if (key.equals(temp))
            {
                return true;
            }
        }

        return false;
    }

    /* (non-Javadoc)
     * @see org.crosswire.jsword.passage.Key#retain(org.crosswire.jsword.passage.Key)
     */
    public void retainAll(Key key)
    {
        Key shared = new DefaultKeyList();
        shared.addAll(key);
        retain(this, shared);
    }

    /**
     * Utility to remove all the keys from alter that are not in base
     * @param alter The key to remove keys from
     * @param base The check key
     */
    protected static void retain(Key alter, Key base)
    {
        for (Iterator it = alter.iterator(); it.hasNext(); )
        {
            Key sublist = (Key) it.next();
            if (sublist.canHaveChildren())
            {
                retain(sublist, base);
                if (sublist.isEmpty())
                {
                    it.remove();
                }
            }
            else
            {
                if (!base.contains(sublist))
                {
                    it.remove();
                }
            }
        }
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    public String toString()
    {
        return getName();
    }

    /**
     * Override the default name with a custom name.
     * If the name is null then a name will be generated by concatenating the
     * names of all the elements of this node.
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /* (non-Javadoc)
     * @see org.crosswire.jsword.passage.Key#getName()
     */
    public String getName()
    {
        if (name != null)
        {
            return name;
        }

        final StringBuffer buffer = new StringBuffer();
        KeyUtil.visit(this, new DefaultKeyVisitor()
        {
            public void visitLeaf(Key key)
            {
                buffer.append(key.getName());
                buffer.append(AbstractPassage.REF_PREF_DELIM);
            }
        });

        String reply = buffer.toString();
        if (reply.length() > 0)
        {
            // strip off the final ", "
            reply = reply.substring(0, reply.length() - AbstractPassage.REF_PREF_DELIM.length());
        }

        return reply;
    }

    /* (non-Javadoc)
     * @see org.crosswire.jsword.passage.Key#getOSISName()
     */
    public String getOSISName()
    {
        if (osisName != null)
        {
            return osisName;
        }

        final StringBuffer buffer = new StringBuffer();
        KeyUtil.visit(this, new DefaultKeyVisitor()
        {
            public void visitLeaf(Key key)
            {
                buffer.append(key.getOSISName());
                buffer.append(AbstractPassage.REF_OSIS_DELIM);
            }
        });

        String reply = buffer.toString();
        if (reply.length() > 0)
        {
            // strip off the final ", "
            reply = reply.substring(0, reply.length() - AbstractPassage.REF_OSIS_DELIM.length());
        }

        return reply;
    }

    /* (non-Javadoc)
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    public int compareTo(Object obj)
    {
        Key that = (Key) obj;

        Key thisfirst = (Key) this.iterator().next();
        Key thatfirst = (Key) that.iterator().next();

        if (thisfirst == null)
        {
            if (thatfirst == null)
            {
                // we are both empty, and rank the same
                return 0;
            }
            else
            {
                // i am empty, he is not so we are greater
                return 1;
            }
        }

        if (thatfirst == null)
        {
            // he is empty, we are not so he is greater
            return -1;
        }

        return thisfirst.compareTo(thatfirst);
    }

    /**
     * The common user visible name for this work
     */
    private String name;

    /**
     * The OSIS ID attribute
     */
    private String osisName;
}
