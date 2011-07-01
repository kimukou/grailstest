package ectest

import com.icegreen.greenmail.util.*

class GreenmailTests extends GroovyTestCase {
    def mailService
    def greenMail

    void testSendMail() {
        Map mail = [message:'こんにちは赤ちゃん', from:'admin@ectest-grails.com', to:'kouichi_kimura@kogasoftware.com', subject:'subject']

        mailService.sendMail {
            to mail.to
            from mail.from
            subject mail.subject
            body mail.message
        }

        assertEquals(1, greenMail.getReceivedMessages().length)

        def message = greenMail.getReceivedMessages()[0]

        assertEquals(mail.message, GreenMailUtil.getBody(message))
        assertEquals(mail.from, GreenMailUtil.getAddressList(message.from))
        assertEquals(mail.subject, message.subject)
    }

    void tearDown() {
        greenMail.deleteAllMessages()
    }
}
