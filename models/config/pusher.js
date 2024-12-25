// filepath: /c:/API/api_evelin/config/pusher.js
const Pusher = require('pusher');

class PusherService {
  constructor() {
    this.pusher = new Pusher({
      appId: process.env.PUSHER_APP_ID,
      key: process.env.PUSHER_KEY,
      secret: process.env.PUSHER_SECRET,
      cluster: 'ap1', // Same cluster as in Android implementation
      useTLS: true
    });
  }

  // Trigger an event on a specific channel
  async triggerEvent(channel, eventName, data) {
    try {
      const response = await this.pusher.trigger(channel, eventName, data);
      console.log(`Event triggered on channel ${channel}: ${eventName}`, response);
      return response;
    } catch (error) {
      console.error('Error triggering Pusher event:', error);
      throw error;
    }
  }

  // Send a notification event
  async sendNotification(channel, title, content, additionalData = {}) {
    try {
      const notificationPayload = {
        title,
        content,
        ...additionalData
      };

      await this.pusher.trigger(channel, 'notification', notificationPayload);
      console.log(`Notification sent on channel ${channel}:`, notificationPayload);
    } catch (error) {
      console.error('Error sending notification:', error);
      throw error;
    }
  }

  // Example method to broadcast event updates (similar to Android implementation)
  async broadcastNewInfo(title, description) {
    try {
      await this.triggerEvent('kerja-praktek', 'New-Announcement', {
        title: title,
        message: description
      });
    } catch (error) {
      console.error('Error broadcasting event update:', error);
    }
  }

  // Broadcast date change specifically
  async broadcastEventDateChange(eventId, oldDate, newDate) {
    try {
      await this.triggerEvent('events', 'event-date-changed', {
        message: `Event date changed from ${oldDate} to ${newDate}`,
        eventId,
        oldDate,
        newDate
      });
    } catch (error) {
      console.error('Error broadcasting event date change:', error);
    }
  }
}

module.exports = new PusherService();