<?xml version="1.0" encoding="UTF-8"?>

<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

    <xsl:template match="/">
        <xsl:for-each select="oceny/przedmiot">
            <h1>
                Przedmiot: <xsl:value-of select="nazwa"/>
            </h1>
            <xsl:for-each select="grupa">
                <h2>
                    Grupa: <xsl:value-of select="nazwa"/>
                </h2>
                <table border="1">
                    <tr bgcolor="grey">
                        <th>Imie</th>
                        <th>Nazwisko</th>
                        <th>Srednia ocen</th>
                        <th>Komentarz</th>
                    </tr>
                    <xsl:for-each select="uczen">
                        <tr>
                            <td>
                                <xsl:value-of select="imie"/>
                            </td>
                            <td>
                                <xsl:value-of select="nazwisko"/>
                            </td>
                            <td>
                                <xsl:value-of select="format-number(sum(ocena/text()) div count(ocena), '0.00')"/>
                            </td>
                            <td>
                                <xsl:choose>
                                    <xsl:when test="sum(ocena/text()) div count(ocena) >= 3.0">
                                        <span style="color:green;">Zaliczone</span>
                                    </xsl:when>
                                    <xsl:otherwise>
                                        <span style="color:red;">Niezaliczone</span>
                                    </xsl:otherwise>
                                </xsl:choose>
                            </td>
                        </tr>
                    </xsl:for-each>
                </table>
            </xsl:for-each>
        </xsl:for-each>
    </xsl:template>
</xsl:stylesheet>