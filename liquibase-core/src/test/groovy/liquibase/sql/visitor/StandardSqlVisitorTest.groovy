package liquibase.sql.visitor

import liquibase.parser.core.ParsedNode
import liquibase.sdk.supplier.resource.ResourceSupplier
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

abstract class StandardSqlVisitorTest extends Specification {

    @Shared resourceSupplier = new ResourceSupplier()

    @Unroll("#featureName setting #field")
    def "load works correctly"() {
        when:
        def visitor = createClass()
        def node = new ParsedNode(null, visitor.getSerializedObjectName())
        def fieldValue = "value for ${field}"
        node.addChild(null, field, fieldValue)
        visitor.load(node, resourceSupplier.simpleResourceAccessor)

        then:
        visitor[field] == fieldValue

        where:
        field << createClass().getSerializableFields().findAll({ !(it in ["applyToRollback", "applicableDbms", "contexts"])})
    }

    def SqlVisitor createClass() {
        Class.forName(getClass().getName().replaceAll('Test$', "")).newInstance()
    }
}