package io.github.vhoyon.vramework.interfaces;

import io.github.vhoyon.vramework.abstracts.AbstractBotCommand;

/**
 * Enum that defines which level should the Buffer saves the object given in
 * appropriate methods.
 * <p>
 * Here's the meaning of the possibilities :
 * </p>
 * <ul>
 * <li>BufferLevel.CHANNEL : Saves the object for a TextChannel, meaning other
 * channels in the same server may not have access to the data stored in here
 * (DEFAULT);</li>
 * <li>BufferLevel.GUILD : Saves the object for a Server (Guild, in Discord's
 * terms), meaning this data could apply to every TextChannel in the same
 * server;</li>
 * <li>BufferLevel.USER : Saves the object for a User's ID, meaning this data is
 * only accessible when this user calls a command.</li>
 * </ul>
 *
 * @version 1.0
 * @since v0.7.0
 * @see AbstractBotCommand#DEFAULT_BUFFER_LEVEL
 */
public enum BufferLevel{
	CHANNEL, GUILD, USER
}
