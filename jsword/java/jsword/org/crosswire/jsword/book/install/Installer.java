package org.crosswire.jsword.book.install;

import java.util.List;
import java.util.Properties;

/**
 * An interface that allows us to download from a specific source of Bible data.
 * 
 * <p>To start with I only envisage that we use Sword sourced Bible data
 * however the rest of the system is designed to be able to use data from
 * e-Sword, OLB, etc.</p>
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
public interface Installer
{
    /**
     * Accessor for the URL
     * @return the source url
     */
    public String getURL();

    /**
     * Get a cached list of names (Strings) that represent downloadable modules.
     * If no list has been retrieved from the remote source using reloadIndex()
     * then we should just return an empty list and not attempt to contact the
     * remote source. See notes on reload for more information.
     * @see Installer#reloadIndex()
     */
    public List getIndex();

    /**
     * Get a Properties file describing one of the index entries
     */
    public Properties getEntry(String name);

    /**
     * Refetch a list of names from the remote source.
     * <b>It would make sense if the user was warned about the implications
     * of this action. If the user lives in a country that persecutes
     * Christians then this action might give the game away.</b>
     */
    public List reloadIndex() throws InstallException;

    /**
     * Download and install a module locally.
     * The name should be one from an index list retrieved from getIndex() or
     * reloadIndex()
     * @param name The module to install
     */
    public void install(String name) throws InstallException;
}