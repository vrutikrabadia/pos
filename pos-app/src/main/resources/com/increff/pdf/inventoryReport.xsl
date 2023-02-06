<?xml version="1.0" encoding="UTF-8"?>
	<xsl:stylesheet version="1.0"
					xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
					xmlns:fo="http://www.w3.org/1999/XSL/Format"
					xmlns:date="http://exslt.org/dates-and-times"
					extension-element-prefixes="date">

	<xsl:decimal-format name="money" decimal-separator="," grouping-separator="."/>
	<xsl:template match="GDK">
		<fo:root font-family="arial">
		<fo:layout-master-set>
			<fo:simple-page-master master-name="A4" page-height="29.7cm" page-width="21cm" margin-top="2cm"
								margin-bottom="2cm" margin-left="2cm" margin-right="2cm"
								>
			<fo:region-body/>
			
			<fo:region-after region-name="invoice-footer" extent="5mm"/>
			</fo:simple-page-master>
		</fo:layout-master-set>
		
		<xsl:for-each select="./GDK_ROW">
		
			<fo:page-sequence master-reference="A4">

				<fo:static-content flow-name="invoice-footer" font-size="7pt">
				<fo:table>
					<fo:table-body>
					<fo:table-row>
						<fo:table-cell text-align="left">
						<fo:block>
						<fo:inline color="#f16366"> Increff </fo:inline>                   
						</fo:block>
						</fo:table-cell>
					</fo:table-row>
					<fo:table-row>
						<fo:table-cell text-align="left">
						<fo:block>
							Address and other details              
						</fo:block>
						</fo:table-cell>
					</fo:table-row>	
					</fo:table-body>
				</fo:table>
				</fo:static-content>
			
				<fo:flow flow-name="xsl-region-body" font-size="8pt">

				<fo:table>
					<fo:table-column column-width="8.5cm"/>
					<fo:table-column column-width="8.5cm"/>

					<fo:table-body>
					<fo:table-row>
						<fo:table-cell text-align="left">
						<fo:block>
							<xsl:value-of select="Company_Name"/>
						</fo:block>
						<fo:block text-align="left">
							<xsl:value-of select="Company_Street_Address"/>, <xsl:value-of select="Company_City"/>&#160;<xsl:value-of select="Company_ZIP_Code"/>
						</fo:block>
						<fo:block text-align="left">
							<fo:inline font-weight="bold">Phone </fo:inline><xsl:value-of select="Company_Phone"/>, <fo:inline font-weight="bold">Email </fo:inline> <xsl:value-of select="Company_Email"/>
						</fo:block>				  
						</fo:table-cell>
						<fo:table-cell text-align="right">
						<fo:block>
							<fo:external-graphic src="https://www.increff.com/wp-content/themes/increff/new-mega-menu/images/logo-new.png" height="20mm" content-height="scale-to-fit"/>
						</fo:block>
						</fo:table-cell>
					</fo:table-row>
					</fo:table-body>

				</fo:table>

				<fo:block space-after="1cm"/>
				
				<fo:table>
					<fo:table-column column-width="8.5cm"/>
					<fo:table-column column-width="8.5cm"/>

					<fo:table-body>
					<fo:table-row height="5mm">
						<fo:table-cell padding-top="1mm" padding-left="2mm"  text-align="left" font-size="10pt" background-color="#f16366" color="#ffffff">
						<fo:block>
							<xsl:value-of select="Report_name"/>
						</fo:block>
						</fo:table-cell>
						<fo:table-cell padding-top="1mm" padding-right="2mm" text-align="right" font-size="10pt" background-color="#f16366" color="#ffffff">
						<fo:block>
							DATE: <xsl:value-of select="Report_date"/>
						</fo:block>	
						</fo:table-cell>
					</fo:table-row>
					</fo:table-body>

				</fo:table>		  

					
				
				<fo:block space-after="2cm"></fo:block>
				<fo:table>
					<fo:table-column column-width="2cm"/>
					<fo:table-column column-width="3cm"/>
					<fo:table-column column-width="3cm"/>
					<fo:table-column column-width="3cm"/>
					<fo:table-column column-width="3cm"/>
					<fo:table-column column-width="3cm"/>
					<fo:table-header font-weight="bold">
					<fo:table-row border-bottom="1px solid #eee">
						<fo:table-cell text-align="left" padding-bottom="2px">
							<fo:block><fo:inline color="#f16366">S.NO.</fo:inline></fo:block>
						</fo:table-cell>
						<fo:table-cell text-align="left" padding-bottom="2px">
							<fo:block><fo:inline color="#f16366">BRAND</fo:inline></fo:block>
						</fo:table-cell>
						<fo:table-cell text-align="left" padding-bottom="2px">
						<fo:block><fo:inline color="#f16366">CATEGORY</fo:inline></fo:block>
						</fo:table-cell>
						<fo:table-cell text-align="left" padding-bottom="2px">
						<fo:block><fo:inline color="#f16366">QUANTITY</fo:inline></fo:block>
						</fo:table-cell>
					</fo:table-row>
					</fo:table-header>
					<fo:table-body>
						<xsl:for-each select="RECORDSET">
							<fo:table-row border-bottom="1px solid #eee">
								<fo:table-cell text-align="left" padding-bottom="2px" padding-top="2px">
								<fo:block>
								<xsl:value-of select="position()"/>
								</fo:block>
							</fo:table-cell>
								<fo:table-cell text-align="left" padding-bottom="2px" padding-top="2px">
								<fo:block>
								<xsl:value-of select="BRAND"/>
								</fo:block>
							</fo:table-cell>
							<fo:table-cell text-align="left" padding-bottom="2px" padding-top="2px">
								<fo:block>
								<xsl:value-of select="CATEGORY"/>
								</fo:block>
							</fo:table-cell>
							<fo:table-cell text-align="left" padding-bottom="2px" padding-top="2px">
								<fo:block>
								<xsl:value-of select="QUANTITY"/>
								</fo:block>
							</fo:table-cell>
							</fo:table-row>
						</xsl:for-each>					
					</fo:table-body>
				</fo:table>
				
				</fo:flow>
			</fo:page-sequence>
		
		</xsl:for-each>
		
		</fo:root>
	</xsl:template>
	</xsl:stylesheet>